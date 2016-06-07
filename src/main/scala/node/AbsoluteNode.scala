package node

import node.codegen.Template

case class AbsoluteNode (override val name:String,
                    val expression1:ExpressionNode,
                    val expression2:ExpressionNode) extends Node(name = name) with Template {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}