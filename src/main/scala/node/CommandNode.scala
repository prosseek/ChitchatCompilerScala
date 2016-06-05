package node

case class CommandNode(override val name:String) extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}
