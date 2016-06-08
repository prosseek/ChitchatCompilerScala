package node

/*
  scheme: id | rep;
 */
case class SchemeNode(override val name:String, val node:Node) extends Node(name = name) {

  def isId() = {
    node.isInstanceOf[IdNode]
  }
  def asId() = {
    if (isId) node.asInstanceOf[IdNode]
    else throw new RuntimeException(s"Not id node")
  }

  def isRep() = {
    node.isInstanceOf[RepNode]
  }
  def asRep() = {
    if (isRep) node.asInstanceOf[RepNode]
    else throw new RuntimeException(s"Not rep node")
  }

  def isChoose() = {
    node.isInstanceOf[ChooseNode]
  }
  def asChoose() = {
    if (isChoose) node.asInstanceOf[ChooseNode]
    else throw new RuntimeException(s"Not choose node")
  }

  def codeGen(progNode:ProgNode) :String = {""}
}
