package node

// function_call: ID '(' expressions ')' ;
case class Function_callNode(override val name:String, override val id:String, val constants:List[ConstantNode])
  extends Node(name = name, id = id)