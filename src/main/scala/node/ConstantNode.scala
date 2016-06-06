package node

import node.codegen.Template

case class ConstantNode(override val name:String) extends Node(name = name) with Template {
  def codeGen(progNode:ProgNode) :String = {
    val value = name
    s"push ${value}"
  }
}

