package node

import scala.collection.mutable.ListBuffer

object Annotation extends Enumeration {
  type Annotation = Value
  val PRIVATE, PUBLIC = Value
}

class TypeNode(override val name:String) extends Node(name = name) {
  var annotation = Annotation.PUBLIC
  var base_type:String = _
  val expressions = ListBuffer[ExpressionNode]()
}