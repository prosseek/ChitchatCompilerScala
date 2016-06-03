package node

import parser.ChitchatParser

import scala.collection.mutable.ListBuffer

// the name of prognode is the script path
case class ProgNode(override val name:String = "") extends Node(name = name) {
  val typedefs = ListBuffer[TypedefNode]()
  val correlations = ListBuffer[CorrelationNode]()
  val situations = ListBuffer[SituationNode]()
  val schemas = ListBuffer[SchemaNode]()
  val valuedefs = ListBuffer[ValuedefNode]()
  val functions = ListBuffer[FunctionNode]()
  val commands = ListBuffer[CommandNode]()

  /**
    * Read nodes into fields
    *
    * @param input
    * @return
    */
  def add(input:Node) = {
    input match {
      case TypedefNode(name, id, annotation, base_name) => typedefs += input.asInstanceOf[TypedefNode]
      case CorrelationNode(name) =>                 correlations += input.asInstanceOf[CorrelationNode]
      case SituationNode(name) =>                   situations += input.asInstanceOf[SituationNode]
      case SchemaNode(name) =>                      schemas += input.asInstanceOf[SchemaNode]
      case ValuedefNode(name) =>                    valuedefs += input.asInstanceOf[ValuedefNode]
      case FunctionNode(name, id, params, block) => functions += input.asInstanceOf[FunctionNode]
      case CommandNode(name) =>                     commands += input.asInstanceOf[CommandNode]
      case _ => throw new RuntimeException(s"wrong node type")
    }
  }

  def getTypedefNode(name:String) : Option[TypedefNode] = {
    val result = typedefs filter (_.id == name)
    if (result.size == 0) return None
    if (result.size > 1) throw new RuntimeException(s"Error, multiple name $name")
    Some(result(0))
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
