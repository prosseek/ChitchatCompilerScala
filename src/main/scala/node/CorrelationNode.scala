package node

import scala.collection.mutable.{ListBuffer, Set => MSet}

case class CorrelationNode(override val name:String, override val id:String) extends Node(name = name, id = id) {
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

  def codeGen(progNode:ProgNode) : String = {
    val correlationNodes = progNode.correlations.toList

    def generate_for_simple(values: List[String]) = {
      val res = new StringBuilder
      res ++= values.mkString("allexists ", " ", "\n")
      res ++= "stop\n"
      res.toString
    }

    if (function_call == null) {
      val info = get(correlationNodes)
      return generate_for_simple(info)
    }
    else {
      ""
    }
  }
}

