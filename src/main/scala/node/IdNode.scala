package node

case class IdNode(override val name:String) extends Node(name = name, id = name) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}
