package node

import scala.collection.mutable.{ListBuffer, Set => MSet}

case class CorrelationNode(override val name:String, val id:String) extends Node(name = name) {
  var function_call:Function_callNode = null
  var values = ListBuffer[ValueNode]()

  /**
    * Update the values
    *
    * @param value
    * @return
    */
  def add(value:ValueNode) = {
    values += value
  }
  def add(fc:Function_callNode) = {
    function_call = fc
  }

  /** Returns all the name of the fully resolved correlated names.
    *
    * ==== Example ====
    * {{{
    *   correlation a = (s, z)
    *   correlation z = (p, q)
    *   correlation s = (k, l)
    *
    *   a.get([a,z,s]) => (p, q, k, l)
    * }}}
    *
    * ==== Idea ====
    * {{{
    *   Building a tree will tell you what is a node and what is a leaf
    *
    *    a
    *   |  \
    *   s   z
    *   |\   |\
    *   k l  p q
    * }}}
    *
    * ==== Algorithm ====
    *  1. From input correlationNames create a tree
    *  2. Given a name (a), find all the leaves
    *
    * @param correlationNodes
    * @return a set of name strings
    */
  def get(correlationNodes:List[CorrelationNode]) : List[String] = {
    util.Tree(correlationNodes).get(this.id)
  }
}

