package node

abstract class Node(val name:String = "", val id:String = "") {
  def codeGen(progNode:ProgNode) : String
}
