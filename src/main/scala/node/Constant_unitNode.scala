package node

import node.codegen.Template

case class Constant_unitNode(override val name:String, val constant:ConstantNode, val unit:String)
  extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = ""
}


