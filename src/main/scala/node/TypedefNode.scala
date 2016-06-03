package node

import scala.collection.mutable.ListBuffer

/*
 * typedef: annotation TYPE id EXT  base_type ;
 * base_type: id '(' expressions ')' ;
 */
case class TypedefNode(override val name:String, val id:String, val annotation: String, val base_name:String) extends Node(name = name) {
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