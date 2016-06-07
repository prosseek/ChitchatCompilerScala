package node

import node.codegen.Template
import scala.collection.mutable.{Map => MMap}

/**
  * === Example1 ===
  * {{{
  * situation partyTime(partyname) = ([date, time] - now) >= 0 _hour
  **
  *read partyname
  *jpeekfalse END
  *f2 partyTime 0
  *END:
  *stop
  *partyTime:
  *read time
  *jpeekfalse END
  *read date
  *jpeekfalse END
  *here
  *distance datetime
  *push 0
  *geq
  *r 0
  *}}}
  *=== Example2 ===
    *{{{
    *   value cityParkCenter(latitude, longitude) = {
    *      latitude = [30, 25, 01, 74]
    *      longitude = [-97, 47, 21, 83]
    *   }
    *   situation nearCityPark() = |[latitude, longitude] - cityParkCenter| <= 5 _km
  *#     f2 nearCityPark 0
    *   END:
    *       stop
    *
    *   nearCityPark:
    *       read latitude
    *       jpeekfalse END
    *       read longitude
    *       jpeekfalse END
    *       push [30, 25, 1, 74]
    *       push [-97, 47, 21, 83]
    *       abs location
    *       push 5000.0
    *       fleq
    *       r 0
    *
*}}}
 *
 */

case class SituationNode (override val name:String,
                          override val id:IdNode,
                          val params:ParamsNode,
                          val expression:ExpressionNode) extends Node(name = name, id = id) with Template {

  /**
    * === Why ===
    *
    *  {{{ From the function parameter (partytime)
    *   situation partyTime(partyname) = ([date, time] - now) >= 0 _hour
    *
    *  generate this assembly code
    *
    *      read partyname
    *      jpeekfalse END
    *  }}}
    *
    * @param endLabel
    * @return
    */
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

  /**
    * === why ===
    *  {{{ From the function body (comparison node)
    *   situation partyTime(partyname) = ([date, time] - now) >= 0 _hour
    *
    *  generate this assembly code (
    *
    *    read time
    *    jpeekfalse END
    *    read date
    *    jpeekfalse END
    *    now
    *    distance datetime
    *    push 0
    *    geq
    *    r 0
    *  }}}
    *
    *  or
    *
    *  {{{
    *     situation nearCityPark() = |(latitude, longitude) - cityParkCenter| <= 5 _km
    *
    *    read latitude
    *    jpeekfalse END
    *    read longitude
    *    jpeekfalse END
    *    push [30, 25, 1, 74]
    *    push [-97, 47, 21, 83]
    *    abs location
    *    push 5000.0
    *    fleq
    *    r 0
    *
    *  }}}
    *
    * === Algorithm ===
    *
    *   1. from the 1st parameter - list [ ... ], get the read/jpeekfalse code
    *   2. from the 2nd parameter, find corresponding values
    *      2.1 if value is predefined, do special process
    *   3. identify the correct function (abs location)
    *   4. process the later part after <=
    *   5. process the operator (fleq/geq)
    *   6. return value
    *
    * @param progNode
    * @param endLabel
    * @return
    */
  def getFunctionBody(progNode:ProgNode, endLabel:String) :String = {
    val res = new StringBuilder

    // only comparsion expression is allowed
    if (expression.isComparison) {
      val c = expression.asComparison

      val map = MMap[String, String]()
      val template =
        """
          |#{code_from_list}
          |#{code_from_value}
          |#{call_function}
          |#{push_value}
          |#{compare}
          |r 0
        """.stripMargin

      val e1 = c.expression1
      val listNode: ListNode = if (e1.isAbsolute || e1.isArithmetic)
        if (e1.isAbsolute) e1.asAbsolute.expression1.asValue.asList
        else e1.asArithmetic.expression1.asValue.asList
      else throw new RuntimeException(s"e1 in comparison node is neither absolute nor arithmetic")

      map("code_from_list") = getCodeFromList(listNode, endLabel)
      map("code_from_value") = ""
      map("call_function") = ""
      map("push_value") = ""
      map("compare") = ""

      getTemplateString(template, map.toMap)
    }
    else
      throw new RuntimeException(s"Only comparison is allowed in situation node ${name}")
  }

  def getCodeFromList(l:ListNode, endLabel:String) : String = {
    val s = new StringBuilder
    l.values foreach {
      value => {
        if (value.isId) {
          val id = value.asId.name
          s ++= s"read ${id}\njpeekfalse ${endLabel}\n"
        }
        else
          throw new RuntimeException(s"only id is allowed in situation node's list")
      }
    }
    return s.toString
  }

  def generateCodeFromParameters() = {
    // progNode.isValue("")
  }
  def processArithmeticNode(arithmeticNode: ArithmeticNode) = {
    val a = arithmeticNode.expression1
//    val list = getListFromExpression(a)
//    println(list)
    val code = generateCodeFromParameters()
    ""
  }
  def processAbsoluteNode(absoluteNode: AbsoluteNode) = {
//    val a = absoluteNode.expression1
//    val list = getListFromExpression(a)
    ""
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
    val endLabel:String = id.name + "_END"
    map("label_end") = endLabel
    map("precode") = getPrecode(endLabel)
    map("function_body") = getFunctionBody(progNode, endLabel)

    getTemplateString(template, map.toMap)
  }
}
