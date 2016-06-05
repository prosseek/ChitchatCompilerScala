package node

case class ListNode(override val name:String, val values:List[ConstantNode]) extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}

