package node

object Annotation extends Enumeration {
  type Annotation = Value
  val PRIVATE, PUBLIC = Value
}

class TypeNode(override val name:String) extends Node(name = name) {
  var annotation = Annotation.PUBLIC
}