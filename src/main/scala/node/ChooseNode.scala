package node

/**
  * choose: (id '|'?)+ ;
  */
case class ChooseNode(override val name:String, val ids:List[IdNode]) extends Node(name = name) {
  def codeGen(progNode:ProgNode) :String = {""}
}
