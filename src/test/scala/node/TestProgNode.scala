package node

import org.scalatest.FunSuite

class TestProgNode extends FunSuite {
  val prognode = NodeGenerator.get("./resources/unittest_example/correlation.txt")

  /**
    * correlation a = (s, z, (h, p, u))
    * correlation z = (p, q, (r, s))
    * correlation s = (k, l)
    */
  test ("simple") {
    val a = prognode.getCorrelationNode("a").get
    assert(a.name == "a")
    print(a.elements.mkString == "suphz")

    assert(prognode.getCorrelationTypeNames("a").get.toString == "Set(u, q, l, p, h, r, k)")
  }
}
