package codegen

import node.NodeGenerator
import org.scalatest.FunSuite

class TestTypeGen extends FunSuite
{
  test ("find range test") {
    val prognode = NodeGenerator.get("./resources/example/input.txt")
    val types = prognode.types.toList

    /*
        -type hour extends Range(size=5, min=0, max=23, signed=false)
        -type markethour extends hour(min=10, max=18)

        * From the specification
        * markethour -> hour(min=10, max=18) (this has the name) -> Range(size=5, signed=false)
        * => Range(name = "hour", min=10, max=18, size=5, signed=false)
     */
    val tg = new TypeGen(typeNode=null, typeNodes = types)
    val result = tg.findRange("markethour")
    val expected = Map[String, String]("name" -> "markethour", "group" -> "Range",
                                       "min" -> "10", "max" -> "18", "size" -> "5", "signed" -> "false")
    assert(result.toList.sorted == expected.toList.sorted)

    assert(tg.rangeMapToString(expected) == "new Range(name = \"markethour\", size = \"5\", min = \"10\", max = \"18\", signed=\"false\")")
  }
}
