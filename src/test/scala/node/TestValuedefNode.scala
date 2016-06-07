package node

import org.scalatest.FunSuite

class TestValuedefNode extends FunSuite {

  /**
    * +type hello extends Range(min=-10, max=10, size=5, signed=true)
    */
  test("simple") {
    val prognode = NodeGenerator.get("./resources/unittest_example/value_simple.txt")
    val a = prognode.getNode[ValuedefNode]("cityParkCenter", prognode.valuedefs).get

    println(a.id)
  }
}