package node

import org.scalatest.FunSuite

class TestFunctionNode extends FunSuite {
  test ("simple") {
    val prognode = NodeGenerator.get("./resources/unittest_example/function_simple.txt")
    val bs = prognode.getNode[FunctionNode]("priceMatch", prognode.functions).get
  }
}