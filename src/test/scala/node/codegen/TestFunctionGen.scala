package node.codegen

import org.scalatest.FunSuite
import node._

class TestFunctionGen extends FunSuite {
  test("simple") {
    val prognode = NodeGenerator.get("./resources/unittest_example/function_simple.txt")
    val bs = prognode.getNode[FunctionNode]("priceMatch", prognode.functions).get
  }
}
