package node

import scala.collection.mutable.ListBuffer

case class TypedefNode(override val name:String, val annotation: String, val base_name:String) extends Node(name = name) {
  val assignments = ListBuffer[AssignmentNode]()
  var function_call:Function_callNode = null
  var values = ListBuffer[ValueNode]()

  def add(assignment: AssignmentNode) = {
    assignments += assignment
  }
  def add(functionCall: Function_callNode) = {
    function_call = functionCall
  }
  def add(valueNode: ValueNode) = {
    values += valueNode
  }
}