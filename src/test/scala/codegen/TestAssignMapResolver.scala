package codegen

import node.{NodeGenerator, TypeNode}
import org.scalatest.FunSuite
import codegen.AssignMapResolver

/*
  +type hello extends Range(min=-10, max=10, size=5, signed=true)
  -type hour extends Range(size=5, min=0, max=23, signed=false)
  -type minute extends Range(size=6, min=0, max=59, signed=false)
  +type time extends Encoding(hour, minute)

  +type temperature extends Float(min=-50.0, max=90.0)
  +type event extends String(alphanum)
  +type name extends String(length < 10)
  -type markethour extends hour(min=10, max=18)
  +type "market time" extends time(markethour)
 */
class TestAssignMapResolver extends FunSuite with AssignMapResolver {

  // def getTypeGroupName(typeNodeName:String, typeNodes:List[TypeNode])
  val prognode = NodeGenerator.get("./resources/unittest_example/type.txt")
  val types = prognode.types.toList

  test("getTypeGroupName test") {
    assert(getTypeGroupName("time", types).toString() == "TypeNode(time,+,Encoding)")
    assert(getTypeGroupName("market time", types).toString() == "TypeNode(time,+,Encoding)")
    assert(getTypeGroupName("markethour", types).toString() == "TypeNode(hour,-,Range)")
  }

  test("getAssignMapFromRangeName test") {
    // (goalRangeName:String, typeNodes:List[TypeNode]) = {)
    var expected = Map("name" -> "markethour",
      "size" -> "5", "min" -> "10", "signed" -> "false", "max" -> "18", "group" -> "Range")
    assert(getAssignMapFromRangeName("markethour", types).toList.sorted == expected.toList.sorted)
  }

  test("getAssignMapFromFloatName test") {
    // (goalRangeName:String, typeNodes:List[TypeNode]) = {)
    var expected = Map("name" -> "temperature",
      "min" -> "-50.0", "max" -> "90.0", "group" -> "Float")
    assert(getAssignMapFromFloatName("temperature", types).toList.sorted == expected.toList.sorted)
  }

  test("get history test") {
    assert(getHistory("market time", types).toString == "List(TypeNode(market time,+,time), TypeNode(time,+,Encoding))")
  }

  test("getRangeNamesFromEncoding test") {
    assert(getRangeNamesFromEncoding("market time", types) == List("hour", "minute"))
  }
}