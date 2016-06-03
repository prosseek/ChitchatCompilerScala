package node

import scala.collection.mutable.ListBuffer

case class FunctionNode(override val name:String, val ID:String, val params:Seq[Any])
  extends Node(name = name)

