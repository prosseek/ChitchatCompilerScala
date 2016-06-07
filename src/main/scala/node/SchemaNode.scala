package node

case class SchemaNode(override val name:String, override val id:IdNode, val annotation:String, val expressions:List[ExpressionsNode]) extends Node(name = name, id = id) {
  def codeGen(progNode:ProgNode) :String = {
    "Not ready yet"
  }
}
