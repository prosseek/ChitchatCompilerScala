package node

// value: id | constant | list ;
case class ValueNode (override val name:String, val node:Node) extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}

