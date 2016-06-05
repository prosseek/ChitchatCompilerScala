package node

case class AssignmentNode(override val name:String,
                          override val id:String,
                          val expression:ExpressionNode) extends Node(name = name, id = id)
{
  /**
    * Given key, returns a value from the expression
    *
    * @param key
    */
  def getValueInString(key:String) : String = {
    if (expression.node.isInstanceOf[ValueNode]) {
      val value = expression.node.asInstanceOf[ValueNode]
      if (value.node.isInstanceOf[ConstantNode]) {
        val const = value.node.asInstanceOf[ConstantNode]
        return const.name
      }
    }
    throw new RuntimeException(s"When use getValue in assignment node, expression should be constant ${expression.name}")
  }

  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}

