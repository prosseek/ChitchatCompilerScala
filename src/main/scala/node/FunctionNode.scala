package node

import scala.collection.mutable.ListBuffer

case class FunctionNode(override val name:String, val id:String, val params:Seq[ValueNode], val block:BlockNode)
  extends Node(name = name)

