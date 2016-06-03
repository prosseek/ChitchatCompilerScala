package node

case class ListNode(override val name:String, val values:List[ConstantNode]) extends Node(name = name)

