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
    ""
  }

  def getFunctionBody() = {
    ""
  }

  def codeGen(progNode:ProgNode) :String = {
    val template =
      """
        |#{precode}
        |f2 #{funcname} 0
        |#{label_end}:
        |  stop
        |#{funcname}:
        |#{function_body}
      """.stripMargin
    val map = MMap[String, String]()


    map("funcname") = id.name
    map("label_end") = id.name + "_END"
    map("precode") = getPrecode(endLabel = map("label_end"))
    map("function_body") = getFunctionBody()

    getTemplateString(template, map.toMap)
  }
}
