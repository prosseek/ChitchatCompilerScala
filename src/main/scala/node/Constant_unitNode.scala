package node

import node.codegen.Template

case class Constant_unitNode(override val name:String, val constant:ConstantNode, val unit:String)
  extends Node(name = name) {

  def unitToValue(value:String, unit:String) = {
    val u =  unit match {
      case "_km" => 1000
      case "_m" => 1
      case "_hour" => 1
      case _ => throw new RuntimeException(s"Wrong unit ${unit}")
    }
    val result = value.toInt * u
    result.toString
  }

  def codeGen(progNode:ProgNode) :String = {
    val value = constant.name
    s"push ${unitToValue(value, unit)}"
  }
}


