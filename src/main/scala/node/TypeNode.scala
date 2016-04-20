package node

import scala.collection.mutable.ListBuffer

case class TypeNode(override val name:String, val annotation: String, val base_name:String) extends Node(name = name) {
  val expressions = ListBuffer[ExpressionNode]()

  def add(expressionNode: ExpressionNode): Unit = {
    expressions += expressionNode
  }
}