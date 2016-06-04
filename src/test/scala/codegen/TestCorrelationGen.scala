package codegen

import node.NodeGenerator
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
//    val a = prognode.getCorrelationNode("a").get
//    assert(a.name == "a")
//    println(a.get(cs))
    val c = prognode.getCorrelationNode("bookseller").get
    val cg = new CorrelationCodeGen(correlationNode = c, correlationNodes = cs.toList)

    println(cg.generate())
  }
}
