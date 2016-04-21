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

  test ("gen test range") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)

    var expect =
      "package chitchat.types\nclass Markethour extends Range ( name = \"markethour\", size = 5, min = 10, max = 18, signed = false )"
    assert(expect == tg.gen("markethour"))
  }

  test ("gen test float") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)

    val expect = "package chitchat.types\nclass Temperature extends Float ( name = \"temperature\", min = -50.0, max = 90.0 )"
    assert(expect == tg.gen("temperature"))
  }

  test("getContentForEncoding test") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)
    val res = tg.getContentForEncoding("market time")
    val expected = """Array[Range](new Range(name = "hour", size = 5, min = 10, max = 18, signed = false),new Range(name = "minute", size = 6, min = 0, max = 59, signed = false))"""
    assert(res == expected)
  }

  test("getContentForRange test") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)
    val res = tg.getContentForRange("markethour")
    val expected = "size = 5, min = 10, max = 18, signed = false"
    assert(res == expected)
  }

  test("getContentForFloat test") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)
    val res = tg.getContentForFloat("temperature")
    val expected = "min = -50.0, max = 90.0"
    assert(res == expected)
  }

  test("getContentForString test") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)
    var res = tg.getContentForString("max10")
    assert(res == "conditions = List(\"maxlength\", 10)")

    res = tg.getContentForString("only a b")
    print(res)
    res = tg.getContentForString("event")
    print(res)
  }

  test ("gen test string max10") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)

    val expect = """package chitchat.types
                   |class Max10 extends String ( name = "max10", conditions = List("maxlength", 10) )""".stripMargin
    assert(expect == tg.gen("max10"))
  }

  test ("gen test string only a b") {
    val tg = new TypeGen(typeNode=null, typeNodes = types)

    val expect = """package chitchat.types
                   |class Only_a_b extends String ( name = "only a b", range = List('a', 'b') )""".stripMargin
    assert(expect == tg.gen("only a b"))
  }
}
