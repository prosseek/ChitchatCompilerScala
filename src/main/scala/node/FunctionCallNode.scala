package node

import scala.collection.mutable.ListBuffer

case class FunctionCallNode(val ID:String, val params:Seq[Any]) extends ExpressionNode

