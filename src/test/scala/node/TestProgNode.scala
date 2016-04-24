package node

import org.scalatest.FunSuite

class TestProgNode extends FunSuite {
  val prognode = NodeGenerator.get("./resources/unittest_example/correlation.txt")

  /**
    * correlation a = (x, y, z, (h, p, u))
    * correlation z = (p, q, (r, s))
    * correlation s = (k, l)
    */
  test ("simple") {
    val a = prognode.getCorrelationNode("a").get
    assert(a.name == "a")
    assert(a.elements.size == 5)

    assert(prognode.getCorrelationTypeNames("a").get.toString == "Set(u, q, l, p, h, r, k)")
  }
}
