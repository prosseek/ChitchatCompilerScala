package node

case class ValuedefNode(override val name:String) extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}
