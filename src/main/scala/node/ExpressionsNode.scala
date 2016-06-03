package node

// ID, STRING, CONSTANT
case class ExpressionsNode(override val name:String, val expressions: List[ExpressionNode])
  extends Node(name = name)


