package node

case class SchemaNode(override val name:String, override val id:String) extends Node(name = name, id = id) {
  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}
