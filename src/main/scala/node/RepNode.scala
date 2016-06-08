package node

/**
  *   rep: '(' (id ','?)+ ')' '+';
  */
case class RepNode(override val name:String, val ids:List[IdNode]) extends Node(name = name) {
  def codeGen(progNode:ProgNode, labels:Map[String, String] = null) :String = {""}
}
