package codegen

import node.NodeGenerator
import org.scalatest.FunSuite

class TestTypeGen extends FunSuite
{
  val prognode = NodeGenerator.get("./resources/example/input.txt")
  val types = prognode.types.toList

  test ("find range test") {
    /*
        -type hour extends Range(size=5, min=0, max=23, signed=false)
        -type markethour extends hour(min=10, max=18)

        * From the specification
        * markethour -> hour(min=10, max=18) (this has the name) -> Range(size=5, signed=false)
        * => Range(name = "hour", min=10, max=18, size=5, signed=false)
     */
    val tg = new TypeGen(typeNode=null, typeNodes = types)
    val result = tg.getAssignMapFromRangeName("markethour", types)
    val map1 = Map[String, String]("name" -> "markethour", "group" -> "Range",
      "min" -> "10", "max" -> "18", "size" -> "5", "signed" -> "false")
    assert(result.toList.sorted == map1.toList.sorted)

    val expected = "new Range(name = \"markethour\", size = \"5\", min = \"10\", max = \"18\", signed=\"false\")"
    assert(tg.rangeMapToString(map1) == expected)
  }

  test ("gen test") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)

    var expect =
      "package chitchat.types\nclass Markethour extends Range ( name = \"markethour\", size = 5, min = 10, max = 18, signed = false )"
    assert(expect == tg.gen("markethour"))

    println(tg.gen("market time"))
  }
}
