package node

import scala.collection.mutable.ListBuffer

case class FunctionNode(override val name:String,
                        val return_type:String,
                        override val id:String,
                        val params:List[String],
                        val block:BlockNode)
  extends Node(name = name, id = id)

