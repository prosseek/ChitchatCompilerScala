package node

import scala.collection.mutable.ListBuffer

case class FunctionNode(val ID:String, val params:Seq[Any]) extends ExpressionNode

