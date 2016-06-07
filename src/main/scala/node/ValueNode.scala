package node

import node.codegen.Template
import scala.collection.mutable.{Map => MMap}

// value: id | constant | list ;
case class ValueNode (override val name:String, val node:Node) extends Node(name = name) with Template {
  def codeGen(progNode:ProgNode) :String = {
    node match {
      case IdNode(name) => node.codeGen(progNode)
      case Constant_unitNode(name, constant, unit) => node.codeGen(progNode)
      case _ => "WHY?"
    }
  }
}

