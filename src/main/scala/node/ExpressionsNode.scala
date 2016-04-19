package node

import collection.mutable.{Map => MMap}

object ExpressionType extends Enumeration {
  type Annotation = Value
  val COMPARISON, ASSIGNMENT, FUNCTION_CALL, PRIMARY_EXPRESSION = Value
}

class ExpressionsNode(override val name:String = "") extends Node(name = name) {
  val assignments = MMap[Integer, Any]()

  def addToAssignment(key:Integer, value:Any) = {
    assignments(key) = value
  }
}
