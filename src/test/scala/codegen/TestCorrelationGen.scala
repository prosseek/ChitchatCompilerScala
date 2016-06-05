package codegen

import node.{CorrelationNode, NodeGenerator}
import org.scalatest.FunSuite

class TestCorrelationGen extends FunSuite
{
  /**
    * correlation location = (latitude, longitude)
    * correlation bookseller = (name, location)
    */
  test ("simple") {
    val prognode = NodeGenerator.get("./resources/unittest_example/correlation_simple.txt")
    val cs = prognode.correlations

    val c = prognode.getNode[CorrelationNode]("bookseller", prognode.correlations).get
    val cg = new CorrelationCodeGen(correlationNode = c, correlationNodes = cs.toList)

    println(cg.generate())
  }
}
