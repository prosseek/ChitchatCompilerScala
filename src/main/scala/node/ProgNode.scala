package node

import parser.ChitchatParser

import scala.collection.mutable.ListBuffer

// the name of prognode is the script path
case class ProgNode(override val name:String = "") extends Node(name = name) {
  val types = ListBuffer[TypeNode]()
  val correlations = ListBuffer[CorrelationNode]()

  /**
    * Read nodes into fields
    *
    * @param input
    * @return
    */
  def add(input:Node) = {
    input match {
      case TypeNode(name, annotation, base_name) => types += input.asInstanceOf[TypeNode]
      case CorrelationNode(name) => correlations += input.asInstanceOf[CorrelationNode]
      case _ => throw new RuntimeException(s"wrong node type")
    }
  }

  /**
    * Returns correlation node from name
    *
    * @param name
    * @return
    */
  def getCorrelationNode(name:String) : Option[CorrelationNode] = {
    val result = correlations filter (_.name == name)
    if (result.size == 0) return None
    if (result.size > 1) throw new RuntimeException(s"Error, multiple name $name")
    Some(result(0))
  }

  /**
    * Returns the full resolved type names
    *
    * @param name
    * @return
    */
  def getCorrelationTypeNames(name:String) : Option[Set[String]] = {
    val result = getCorrelationNode(name)
    if (result.isEmpty) return None
    Some(result.get.get(correlations))
  }
}
