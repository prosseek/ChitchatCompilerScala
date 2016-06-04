package node

case class LogicNode(
                      override val name:String,
                      val expression1:ExpressionNode,
                      val op:String,
                      val expression2:ExpressionNode) extends Node(name = name)

