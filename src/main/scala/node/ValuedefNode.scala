package node

case class ValuedefNode(override val name:String, override val id:IdNode, val map:Map[String, String]) extends Node(name = name, id = id) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}
