package node

case class ExpressionNode(override val name:String, val node:Node) extends Node(name = "")

