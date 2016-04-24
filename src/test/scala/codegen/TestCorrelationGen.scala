package codegen

import node.NodeGenerator
import org.scalatest.FunSuite

/**
  * Created by smcho on 4/22/16.
  */
class TestCorrelationGen extends FunSuite
{
  val prognode = NodeGenerator.get("./resources/unittest_example/correlation.txt")

  test ("simple") {
    val cs = prognode.correlations
    val a = prognode.getCorrelationNode("a").get
    assert(a.name == "a")
    println(a.get(cs))
  }
}
