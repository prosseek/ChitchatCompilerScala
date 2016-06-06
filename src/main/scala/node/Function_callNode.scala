package node

// function_call: ID '(' expressions ')' ;
case class Function_callNode(override val name:String, override val id:IdNode, val constants:List[ConstantNode])
  extends Node(name = name, id = id) {

  def codeGen(progNode:ProgNode) :String = {
    ""
  }
}