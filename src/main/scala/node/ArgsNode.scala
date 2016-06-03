package node

case class ArgsNode(override val name:String, val constants:List[ConstantNode]) extends Node(name = name)


