package node

case class BlockNode(override val name:String, val expressions:List[ExpressionNode]) extends Node(name = name)
