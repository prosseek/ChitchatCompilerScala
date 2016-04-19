package node

import parser.ChitchatParser

import scala.collection.mutable.ListBuffer

/**
  * Created by smcho on 4/18/16.
  */
class ProgNode(override val name:String = "") extends Node(name = name) {

  val types = ListBuffer[TypeNode]()

  def add(input:Node) = {
    input match {
      case input:TypeNode => types += input
      case _ => throw new RuntimeException(s"wrong node type")
    }
  }
}
