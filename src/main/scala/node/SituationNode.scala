package node

import node.codegen.Template
import scala.collection.mutable.{Map => MMap}

/**
  * === Example1 ===
  **
{{{
  * situation partyTime(partyname) = ([date, time] - now) >= 0 _hour
 **
 #         read partyname
    *#     jpeekfalse END
    *#     f2 partyTime 0
    *# END:
    *#     stop
    *# partyTime:
    *#     read time
    *#     jpeekfalse END
    *#     read date
    *#     jpeekfalse END
    *#     here
    *#     distance datetime
    *#     push 0
    *#     geq
    *#     r 0
*}}}
 **
 === Example2 ===
*{{{
    *value cityParkCenter(latitude, longitude) = {
      *latitude = [30, 25, 01, 74]
      *longitude = [-97, 47, 21, 83]
    *}
    *situation nearCityPark() = |[latitude, longitude] - cityParkCenter| <= 5 _km
 **
     #     f2 nearCityPark 0
    *# END:
    *#     stop
    *#
    *# nearCityPark:
    *#     read latitude
    *#     jpeekfalse END
    *#     read longitude
    *#     jpeekfalse END
    *#     push [30, 25, 1, 74]
    *#     push [-97, 47, 21, 83]
    *#     abs location
    *#     push 5000.0
    *#     fleq
    *#     r 0
    *#
*}}}
 *
 */

case class SituationNode (override val name:String,
                          override val id:IdNode,
                          val params:ParamsNode,
                          val expression:ExpressionNode) extends Node(name = name, id = id) with Template {

  def getPrecode(endLabel:String) = {
    val res = new StringBuilder()
    val ps = params.ids map {
      id => res ++= s"read ${id.name}\njpeekfalse ${endLabel}\n"
    }
    res.toString
  }

  def unitToValue(value:String, unit:String) = {
    val u =  unit match {
      case "_km" => 1000
      case "_m" => 1
      case "_hour" => 1
      case _ => throw new RuntimeException(s"Wrong unit ${unit}")
    }
    val result = value.toInt * u
    result.toString
  }

  def getFunctionBody(progNode:ProgNode) :String = {
    def getListFromExpression(e:ExpressionNode) : ListNode = {
      // expression -> value -> list
      val v = e.node.isInstanceOf[ValueNode]
      if (v) {
        val l = v.asInstanceOf[ValueNode].node.isInstanceOf[ListNode]
        if (l) {
          return v.asInstanceOf[ValueNode].node.asInstanceOf[ListNode]
        }
      }
      throw new RuntimeException(s"not found list in the expression")
    }
    def generateCodeFromParameters() = {
      // progNode.isValue("")
    }
    def processArithmeticNode(arithmeticNode: ArithmeticNode) = {
      val a = arithmeticNode.expression1
      val list = getListFromExpression(a)
      println(list)
      val code = generateCodeFromParameters()
      ""
    }
    def processAbsoluteNode(absoluteNode: AbsoluteNode) = {
      ""
    }

    val res = new StringBuilder
    // 1. get the [list]
    if (expression.node.isInstanceOf[ComparisonNode]) {
      val c = expression.node.asInstanceOf[ComparisonNode]

      // a >= 40 _km <- a is the e1
      val e1 = c.expression1.node
      val t1 = e1.isInstanceOf[ArithmeticNode]
      val t2 = e1.isInstanceOf[AbsoluteNode]

      if (t1) { // arithmetic node
        res ++= processArithmeticNode(e1.asInstanceOf[ArithmeticNode])
      }
      else if (t2) { // absolute node
        res ++= processAbsoluteNode(e1.asInstanceOf[AbsoluteNode])
      }
      else {
        throw new RuntimeException(s"only arithmetic node or absolute node can be used for e1 not ${e1.name}")
      }

      // a >= 40 _km <- 40 _km is the e2
      val e2 = c.expression2
      if (e2.node.isInstanceOf[ValueNode] &&
          e2.node.asInstanceOf[ValueNode].node.isInstanceOf[Constant_unitNode]) {
        val constant_unit = e2.node.asInstanceOf[ValueNode].node.asInstanceOf[Constant_unitNode]
        val unitNumber = unitToValue(constant_unit.constant.name, constant_unit.unit)
        res ++= unitNumber
      }
      else {
        throw new RuntimeException(s"In comparison, with a >= b format b should be unit constant ${e2.name}")
      }
      val op = c.op

      res.toString
    }
    else
      throw new RuntimeException(s"Only comparison is allowed in situation node ${name}")
  }

  def codeGen(progNode:ProgNode) :String = {
    val template =
      """
        |#{precode}
        |f2 #{funcname} 0
        |#{label_end}:
        |stop
        |#{funcname}:
        |#{function_body}
      """.stripMargin
    val map = MMap[String, String]()

    map("funcname") = id.name
    val e:String = id.name + "_END"
    map("label_end") = e
    map("precode") = getPrecode(e)
    map("function_body") = getFunctionBody(progNode)

    getTemplateString(template, map.toMap)
  }
}
