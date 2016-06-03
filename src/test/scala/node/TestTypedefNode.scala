package node

import org.scalatest.FunSuite

class TestTypedefNode extends FunSuite {

  /**
    * +type hello extends Range(min=-10, max=10, size=5, signed=true)
    */
  test("simple") {
    val prognode = NodeGenerator.get("./resources/unittest_example/type_simple.txt")
    val a = prognode.getTypedefNode("hello").get
    assert(a.name == "+typehelloextendsRange(min=-10,max=10,size=5,signed=true)")
    assert(a.id == "hello")
    assert(a.assignments.size == 4)
    assert(a.values.size == 0)
    assert(a.function_call == null)
  }

  /**
    * +type time extends Encoding(hour, minute)
    */
  test("simple2") {
    val prognode = NodeGenerator.get("./resources/unittest_example/type_simple2.txt")
    val a = prognode.getTypedefNode("time").get
    assert(a.id == "time")
    assert(a.assignments.size == 0)
    assert(a.values.size == 2)
    assert(a.function_call == null)
  }

  /**
    * +type max10 extends String(maxlength(10))
    */
  test("simple3") {
    val prognode = NodeGenerator.get("./resources/unittest_example/type_simple3.txt")
    val a = prognode.getTypedefNode("max10").get
    assert(a.name == "+typemax10extendsString(maxlength(10))")
    assert(a.id == "max10")
    assert(a.assignments.size == 0)
    assert(a.values.size == 0)

    val f = a.function_call
    assert(f.name == "maxlength(10)")
    assert(f.id == "maxlength")
    assert(f.constants.size == 1)
  }
}