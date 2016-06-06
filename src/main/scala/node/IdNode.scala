package node

import node.codegen.Template
import scala.collection.mutable.{Map => MMap}

case class IdNode(override val name:String) extends Node(name = name) with Template {
  def codeGen(progNode:ProgNode) :String = {
    val bpName = progNode.context.parameterTranslate(name)
    // when bpName == name, the name is not a parameter
    if (bpName == name)
      s"push ${name}"
    else
      s"load ${bpName}"
  }
}
