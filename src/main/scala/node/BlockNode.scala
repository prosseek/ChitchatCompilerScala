package node

import node.codegen.Template

case class BlockNode(override val name:String,
                     val expressions:ExpressionsNode) extends Node(name = name) with Template {
  def codeGen(progNode:ProgNode) :String = {
    expressions.codeGen(progNode)
  }
}
