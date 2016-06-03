package node

// function_call: ID '(' expressions ')' ;
case class Function_callNode(override val name:String, val params:List[ExpressionNode])
  extends Node(name = name)