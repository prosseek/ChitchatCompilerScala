package node

case class ComparisonNode(
                          override val name:String,
                          val op:String,
                          val id:IdNode,
                          val expression:ExpressionNode) extends Node(name = name)

