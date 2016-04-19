package node

import collection.mutable.{Map => MMap}
class ExpressionNode(override val name:String = "") extends Node(name = name) {
  var key: String = ""
  var value: Any = 0
}
