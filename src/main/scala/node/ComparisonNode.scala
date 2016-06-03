package node

case class ComparisonNode(
                          override val name:String,
                          val op:String,
                          val expression1:ExpressionNode,
                          val expression2:ExpressionNode) extends Node(name = name)

