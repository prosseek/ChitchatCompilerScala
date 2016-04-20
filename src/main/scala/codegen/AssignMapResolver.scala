package codegen

import node.{AssignNode, PrimaryExpressionNode, TypeNode}

import collection.mutable.{ListBuffer, Map => MMap}

trait AssignMapResolver {

  /* PRIVATE METHODS */

  private def isParentInGroups(parentName: String) = {
    val set = Set("Range", "Encoding", "String", "Float")
    set.contains(parentName)
  }

  /**
    * Given a history of typenodes (the later the higher in hierarchy, superclass)
    *
    * ==== Example ====
    *  when a(x = 10, y = 20) -> b(x = 20) -> c (z = 30) when a < b < c
    *  the output is (x = 10, y = 20, z = 30) the subclass values overwrite the superclass values
    *
    * @param history
    * @return
    */
  def getAssignMapFromHistory(history:List[TypeNode]) = {
    val map = MMap[String, String]()
    history.reverse foreach {
      typeNode => {
        typeNode.expressions foreach {
          expression => {
            val e = expression.asInstanceOf[AssignNode]
            val key = e.ID
            var value = e.node.value
            map(key) = value
          }
        }
      }
    }
    map
  }

  /**
    * Given a range time, returns all the hierarchical history up to the type group.
    *
    * ==== Example ====
    *  1. a extends b
    *  2. b extends c
    *  3. c extends Range
    *
    *  input : a => output [a][b][c] as a list of nodes
    *
    * @param goalRangeName
    * @param typeNodes
    * @return
    */
  def getHistory(goalRangeName:String, typeNodes:List[TypeNode]) = {
    def getTypeNode(goalRangeName:String) = {
      val typeNode = typeNodes find (_.name == goalRangeName)
      if (typeNode.isEmpty) throw new RuntimeException(s"No ${goalRangeName} in types")
      typeNode.get
    }

    val typeHierarchy = ListBuffer[TypeNode]()

    // 1. check if the goalRangeName is in the type database
    //    set current node into the hierarchy
    val typeNode = getTypeNode(goalRangeName)
    typeHierarchy += typeNode

    // 2. get the parentNames and store them into database
    var parentName = typeNode.base_name
    while (!isParentInGroups(parentName)) {
      val typeNode = getTypeNode(parentName)
      typeHierarchy += typeNode
      parentName = typeNode.base_name
    }
    typeHierarchy.toList
  }

  /** Given goalRangeName, it finds the Range with all the adjusted assignments
    *
    * ==== example ====
    * Given
    *
    *  -type hour extends Range(size=5, min=0, max=23, signed=false)
    *  -type markethour extends hour(min=10, max=18)
    *
    *   The types are stored in `val typeNodes:List[TypeNode]`
    *
    *   When input == "makehour"
    *
    *   Returns a map of ("name" -> "makehour, "group" -> "Range",
    *                     "size"->"5", "min"->"10", "max=18", "signed=false")
    *
    * ==== Algorithm ====
    *  1. Find the history of parents upto one of the four type groups
    *     * markethour -> hour -> Range
    *     * hour has Assign expressions
    *  2. In reverse order, fill in the map
    *     * from hour set size/min/max/signed
    *     * from markethour update min/max
    *  3. return the map
    *
    * ==== Note ====
    *  1. It uses `getHistory` to get all the type nodes from the input to the node whose paresnt is Group Node
    *  2. It uses `getAssignMapFromHistory` to build assignment map from retrieved history of TypeNodes.
    */
  def getAssignMapFromRangeName(rangeName:String, typeNodes:List[TypeNode]) = {
    val map = MMap[String, String]()
    map ++= Map("name" -> rangeName, "group" -> "Range")
    val history = getHistory(rangeName, typeNodes)
    map ++= getAssignMapFromHistory(history)
    map.toMap
  }

  /**
    * Given a typeNodeName, it returns the group where the typeNode belongs to
    *
    * ==== Example ====
    * a extend b -> b extends Range
    *
    * Given a, this function returns the string "Range"
    *
    * @return
    */
  def getTypeGroupName(typeNodeName:String, typeNodes:List[TypeNode]) = {
    /**
      * Given typeNode (as name, not node), finds the ultimate (the node whose parent is one of the four groups)
      * node (not name)
      *
      * ==== Example ====
      *
      *  a extend b, b extends c, c extends Range
      *
      *  getNodeWhoseParentIsTypeGroup(a) returns [c extends Range] as a typeNode
      *
      * @param typeNode
      * @return
      */
    def getNodeWhoseParentIsTypeGroup(typeNode:TypeNode) : TypeNode = {
      var parent = typeNode

      // make an advancement to test
      var parentName = typeNode.base_name
      while (!isParentInGroups(parentName)) {

        val result = typeNodes find (_.name == parentName)
        if (result.isDefined) {
          // make an advancement, the parentName should be one of the four to break the loop
          parent = result.get
          parentName = result.get.base_name
        }
        else // error as one of the type is not in the database
          throw new RuntimeException(s"${parentName} is not in the type names (database)")
      }
      parent
    }

    // 1. find typeNode that should be in the type nodes (database)
    val _typeNode = (typeNodes find (_.name == typeNodeName))
    if (_typeNode.isEmpty)
      throw new RuntimeException(s"Type ${typeNodeName} is not available")
    val typeNode = _typeNode.get

    // 2. get the group node name
    getNodeWhoseParentIsTypeGroup(typeNode)
  }

  /**
    * Given a type name (of a node), it calculates the final group
    * and returns
    *
    * ==== Example ====
    *  type a extends b -> typ b extends Encoding (x, y)
    *
    *  Given a (name), it returns x and y as a list of strings List(x,y)
    *
    * ==== Warning ====
    *  1. It assumes that the Encoding should have parameters as primary expression, not assignment.
    *     In other words Encoding(x, y) not Encoding(x = 10, y = 20)
    *  2. The typeNodeName should be in "Encoding group" otherwise, it will raise an error.
    *
    * @param typeNodeName
    */
  def getRangeNamesFromEncoding(typeNodeName:String, typeNodes:List[TypeNode]) = {
    val typeGroupNode = getTypeGroupName(typeNodeName, typeNodes)
    if (typeGroupNode.base_name != "Encoding")
      throw new RuntimeException(s"${typeNodeName} is not in Encoding group, but ${typeGroupNode.base_name}")
    val res = typeGroupNode.expressions map {
      expression => expression.asInstanceOf[PrimaryExpressionNode].value
    }
    res.toList
  }
}
