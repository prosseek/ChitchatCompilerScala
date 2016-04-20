package node

case class ComparisonNode(val ID:String, val op:String, val node:PrimaryExpressionNode) extends ExpressionNode

