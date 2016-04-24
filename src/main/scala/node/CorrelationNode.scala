package node

import scala.collection.mutable.{ListBuffer, Set => MSet}

case class CorrelationNode(override val name:String) extends Node(name = name) {
  val elements = ListBuffer[String]()

  /** Stores the type names. A progname node has multiple correlatin nodes.
    *
    * @param typeNames
    * @return
    */
  def add(typeNames:List[String]) = {
    elements ++= typeNames
  }

  /** Returns all the name of the correlated names.
    *
    * ==== Example ====
    * {{{
    *   correlation a = (s, z, (h, p, u))
    *   correlation z = (p, q, (r, s))
    *   correlation s = (k, l)
    *
    *   a.get([a,z,s]) => (k, l, p, q, r, s, h, p, u)
    * }}}
    *
    * ==== Algorithm ====
    *  {{{
    *    1. given name, put all the elements.
    *       (s, z, h, p, u)
    *    2. iterate
    *       1. if name is in the set, replace
    *          (k, l, z, h, p, u)
    *       2. iterate from the start
    *    3. when all elements are type (not correlation), stops
    *  }}}
    *
    */
  def get(correlationNodes:ListBuffer[CorrelationNode]) : Set[String] = {
    val correlationNames = (correlationNodes map { node => node.name}).toSet
    def correlationInSet(name:String) = correlationNames.contains(name)
    def noCorrelationInSet(names:Set[String]) = names.filter(correlationInSet(_)).size == 0

    var result = MSet[String]()
    result ++= elements.toSet

    // 1. put all the elements
    while(!noCorrelationInSet(result.toSet)) {
      val temp = MSet[String]()
      result foreach { value =>
        if (correlationInSet(value)) {
          val theNode = correlationNodes.filter(_.name == value)
          if (theNode.size != 1) throw new RuntimeException(s"there are multiple ${value}")
          temp ++= theNode(0).get(correlationNodes)
        }
        else {
          temp += value
        }
      }
      result = temp
    }
  result.toSet
  }
}
