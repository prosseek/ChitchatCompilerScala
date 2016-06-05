package node

case class ConstantNode(override val name:String) extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}

