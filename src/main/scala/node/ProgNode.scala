package node

import parser.ChitchatParser

import scala.collection.mutable.ListBuffer

// the name of prognode is the script path
case class ProgNode(override val name:String = "") extends Node(name = name) {
  val types = ListBuffer[TypeNode]()

  def add(input:Node) = {
    input match {
      case TypeNode(name, annotation, base_name) => types += input.asInstanceOf[TypeNode]
      case _ => throw new RuntimeException(s"wrong node type")
    }
  }
}
