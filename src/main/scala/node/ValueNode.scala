package node

case class ValueNode (override val name:String, val node:Node) extends Node(name = name)

