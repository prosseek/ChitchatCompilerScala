package node

import node.codegen.Template
import scala.collection.mutable.{Map => MMap}

object IdNode {
  def make(name:String) = {
    IdNode(name = name.replace("\"", ""))
  }
}

case class IdNode(override val name:String) extends Node(name = name) with Template {
  def codeGen(progNode:ProgNode, labels:Map[String, String] = null) :String = {
    val bpName = progNode.parameterTranslate(name)
    // when bpName == name, the name is not a parameter
    if (bpName == name)
      s"push ${name}\n"
    else
      s"load ${bpName}\n"
  }

  def schemaCodeGen(progNode:ProgNode, endLabel:String) :String = {
    val code = s"read ${name}\njpeekfalse ${endLabel}\nregister ${name}\n"
    code
  }
}
