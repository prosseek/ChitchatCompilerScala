package node

case class AssignmentNode(override val name:String, val ID:String, val expression:ExpressionNode) extends Node(name = name)
{
  /**
    * Given key, returns a value from the expression
    * @param key
    */
  def getValue(key:String) = {
    if (expression.node.isInstanceOf[ConstantNode]) {
      val const = expression.node.asInstanceOf[ConstantNode]
    }
    else {
      throw new RuntimeException(s"When use getValue in assignment node, expression should be constant ${expression.name}")
    }
  }
}

