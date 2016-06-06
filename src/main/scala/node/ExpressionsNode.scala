package node

// ID, STRING, CONSTANT
case class ExpressionsNode(override val name:String, val expressions: List[ExpressionNode])
  extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = {
    expressions.foldLeft("")((acc, value) => acc + value.codeGen(progNode) + "\n")
  }
}


