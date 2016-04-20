package codegen

import node.{AssignNode, TypeNode}

import scala.collection.mutable.{ListBuffer, Map => MMap}


/*
  package chitchat.types
  class A extends Range (name = "a", size = 5, min = 0, max = 10, correlatedLabels = List("b", "c"))

  class D extends Encoding(
  name = "d",
  Array[Range](
    new Range(name = "y", size = 7, min = -64, max = 63, signed = true),
    new Range(name = "m", size = 4, min =   1, max = 12, signed = false),
    new Range(name = "d", size = 5, min =   1, max = 31, signed = false)))

  class E extends chitchat.types.Float (name = "e")
  class F extends chitchat.types.String (name = "f")
*/

// todo: How to convert the expression into Chitchat type
//       +type Event extends String(alphanum), +type Name extends String(length < 10)
//

class TypeGen(val typeNode:TypeNode, val typeNodes:List[TypeNode]) extends Gen {

  def getTemplateString(template:String, replacement:Map[String, String]) = {
    replacement.foldLeft(template)((s:String, x:(String,String)) => ( "#\\{" + x._1 + "\\}" ).r.replaceAllIn( s, x._2 ))
  }

  def getClassName(name:String) = {
    name.replace("\"","").capitalize.replace(" ", "_")
  }

  val plugin_template =
    s"""package chitchat.types
       |class #{class_name} extends #{parent_name} ( name = #{name}, #{contents} )
    """.stripMargin

  /**
    * Given a typeNodeName, returs thg group where the typeNode belongs
    *
    * @return
    */
  def findTypeGroup(typeNodeName:String) = {
    val typeNode = (typeNodes find (_.name == typeNodeName)).get
    // get all the parents, and find the basic assignments
    val parents = ListBuffer[TypeNode]()
    parents += typeNode

    var parentName = typeNode.base_name
    while (!isParentInGroups(parentName)) {

      val result = typeNodes find (_.name == parentName)
      if (result.isDefined) {
        parents += result.get
        parentName = result.get.base_name
      }
      else // break out of the loop
        parentName = ""
    }
    val res = parents.last.base_name // get the last element
    s"${res}"
  }

  private def isParentInGroups(parentName: String) = {
    val set = Set("Range", "Encoding", "String", "Float")
    set.contains(parentName)
  }

  def rangeMapToString(map:Map[String, String]) = {
    // new Range(name = "y", size = 7, min = -64, max = 63, signed = true)

    val range_template =
      s"""new #{group}(name = "#{name}", size = "#{size}", min = "#{min}", max = "#{max}", signed="#{signed}")""".stripMargin

    getTemplateString(range_template, map)
  }

  /**
    *
    * ==== example ====
    *
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
    */
  def findRange(goalRangeName:String) = {
    def getTypeNode(goalRangeName:String) = {
      val typeNode = typeNodes find (_.name == goalRangeName)
      if (typeNode.isEmpty) throw new RuntimeException(s"No ${goalRangeName} in types")
      typeNode.get
    }

    val map = MMap[String, String]()
    map ++= Map("name" -> goalRangeName, "group" -> "Range")
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
    // 3. from reverse order fill in the map
    typeHierarchy.toList.reverse foreach {
      typeNode => {
        typeNode.expressions foreach {
          expression => {
            val e = expression.asInstanceOf[AssignNode]
            val key = e.ID
            var value = e.node.value.asInstanceOf[Int]
            if (key == "signed") {
              var v = "false"
              if (value == "1") v = "true"
              map(key) = v
            }
            else
              map(key) = value.toString
          }
        }
      }
    }
    map
  }

  def getContent(typeString:String) = {
    typeString match {
      case typeString if typeString == "Range" => {
        val min = 1
        s"min = $min"
      }
      case typeString if typeString == "Encoding" => {
        s"e"
      }
      case typeString if typeString == "Float" => {
        s"f"
      }
      case typeString if typeString == "String" => {
        s"s"
      }
      case _ => throw new RuntimeException(s"wrong thype string ${typeString}")
    }
  }

  /**
    * API gen()
    */
  def gen() = {
    // get the type name
    val typeString = findTypeGroup(typeNode.name)
    val contentString = getContent(typeString)

    val map = Map[String, String](
      "class_name" -> getClassName(typeNode.name),
      "parent_name" -> findTypeGroup(typeNode.name),
      "name" -> typeNode.name, 
      "contents" -> contentString)
    println(getTemplateString(plugin_template, map))
  }
}
