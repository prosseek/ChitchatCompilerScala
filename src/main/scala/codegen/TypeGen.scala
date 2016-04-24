package codegen

import node.{AssignNode, Node, PrimaryExpressionNode, TypeNode}

import scala.collection.mutable.{ListBuffer, Map => MMap}

// todo: How to convert the expression into Chitchat type
//       +type Event extends String(alphanum), +type Name extends String(length < 10)
//

class TypeGen(val typeNode:TypeNode, val typeNodes:List[TypeNode]) extends Gen with AssignMapResolver {

  private def getTypeNodeFromName(typeNodeName:String) = {
    val typeNode = typeNodes find (_.name == typeNodeName)
    if (typeNode.isEmpty) throw new RuntimeException(s"No node type ${typeNodeName} found")
    typeNode.get
  }

  /**
    * Given a map with assignment elements, return a string that interpolates it.
    *
    * @param map
    * @return
    */
  def rangeMapToString(map:Map[String, String]) = {
    // new Range(name = "y", size = 7, min = -64, max = 63, signed = true)

    val range_template =
      s"""new #{group}(name = "#{name}", size = "#{size}", min = "#{min}", max = "#{max}", signed="#{signed}")""".stripMargin

    getTemplateString(range_template, map)
  }

  /**
    * get the contents for encoding type group
    * ==== Example Encoding ====
    *  1. get the number of ranges
    *    Given "market time" type name -> time -> Encoding(hour, minute)
    *    "hour" & "minute" is returned. So, we know this encoding has two ranges.
    *  2. From the input parameter, we know the range assignment
    *     * ex) +type "market time" extends time(markethour)
    *     * =>from 'markethour' parameter & types, we have history 'markethour' -> 'hour' -> Range
    *     * => we already know we need range for "hour" and "minute"
    *  3. We resolve to get assignments for markethour(hour) & minute
    *
    * {{{
    *    class D extends Encoding(
    *    name = "day",
    *    Array[Range](
    *      new Range(name = "y", size = 7, min = -64, max = 63, signed = true),
    *      new Range(name = "m", size = 4, min =   1, max = 12, signed = false),
    *      new Range(name = "d", size = 5, min =   1, max = 31, signed = false)))
    * }}}
    *
    * @param typeNodeName
    * @return
    */
  def getContentForEncoding(typeNodeName:String) = {
    val typeNode = getTypeNodeFromName(typeNodeName)
    val historyList = typeNode.expressions map {
      expression => {
        // the primary expression node has typeValue and value
        // the value has the name of the type
        val node = expression.asInstanceOf[PrimaryExpressionNode]
        getHistory(node.value, typeNodes)
      }
    }
    // get all of the ranges in the encoding
    val rangeNamesWithoutHistory = ListBuffer(getRangeNamesFromEncoding(typeNodeName, typeNodes):_*)
    historyList foreach {
      history => {
        // from historyList we know the range name that has history
        // it is removed
        rangeNamesWithoutHistory -= history.last.name
      }
    }
    // We have two sets of range
    // 1. in rangeNames, the range that does not have history
    // 2. in historyList, the range that does have history
    // merge them into one historyList
    rangeNamesWithoutHistory foreach {
      rangeName => {
        val typeNode = getTypeNodeFromName(rangeName)
        historyList += List[TypeNode](typeNode)
      }
    }

    val rangeContentStrings = historyList map {
      history => {
        val map = getAssignMapFromHistory(history)
        map("name") = history.last.name
        val template = s"""new Range(name = "#{name}", size = #{size}, min = #{min}, max = #{max}, signed = #{signed})"""
        getTemplateString(template, map.toMap)
      }
    }
    rangeContentStrings.mkString("Array[Range](", ",", ")")
  }

  /**
    * ==== Example Range ====
    * {{{
    *  groupString:Range, typeNodeName:"market time"
    *
    *  --> class Markethour extends Range ( name = "markethour", size = 5, min = 10, max = 18, signed = false )
    * }}}
    *
    * @param typeNodeName
    * @return
    */
  def getContentForRange(typeNodeName:String) = {
    val template = s"size = #{size}, min = #{min}, max = #{max}, signed = #{signed}"
    val map = getAssignMapFromRangeName(typeNodeName, typeNodes)
    getTemplateString(template, map)
  }

  /**
    * ==== Example ====
    * +type temperature extends Float(min=-50.0, max=90.0)
    *
    * @param typeNodeName
    * @return
    */
  def getContentForFloat(typeNodeName:String) = {
    val template = s"min = #{min}, max = #{max}"
    val map = getAssignMapFromFloatName(typeNodeName, typeNodes)
    getTemplateString(template, map)
  }

  /**
    * class FTest2 extends String (name = "f", conditions = List(97, 'b'))
    *
    * @param typeNodeName
    * @return
    */
  def getContentForString(typeNodeName:String) = {
    val map = getMapFromStringName(typeNodeName, typeNodes)

    var template = ""
    if (map.get("type").get == "assign")
    {
      template = s"range = List(#{min}, #{max})"
    }
    else {
      template = s"""conditions = List("#{function_name}", #{value})"""
    }
    getTemplateString(template, map)
  }

  /**
    * Returns a type class content from groupString & typeNodeName
    *
    * @param groupString
    * @param typeNodeName
    * @return
    */
  def getContent(groupString:String, typeNodeName:String) : String = {
    if (groupString == "Range") {
      getContentForRange(typeNodeName)
    }
    else if (groupString == "Encoding") {
      getContentForEncoding(typeNodeName)
    }
    else if (groupString == "Float") {
      getContentForFloat(typeNodeName)
    }
    else if (groupString == "String") {
      getContentForString(typeNodeName)
    }
    else {
      throw new RuntimeException(s"wrong thype string ${groupString}")
    }
  }

  /** Given type node name as a string returns the Scala source code
    * The contents are generated from `getContent` method.
    *
    * @param typeNodeName
    * @return
    */
  def gen(typeNodeName:String) = {
    val plugin_template =
      s"""package chitchat.types
          |class #{class_name} extends #{type_group_name} ( name = "#{name}", #{contents} )""".stripMargin

    // get the type name
    val typeGroup = getTypeGroupName(typeNodeName, typeNodes)
    val typeGroupName = typeGroup.base_name
    val contentString = getContent(typeGroupName, typeNodeName)

    val map = Map[String, String](
      "class_name" -> getClassName(typeNodeName),
      "type_group_name" -> typeGroupName,
      "name" -> typeNodeName,
      "contents" -> contentString)
    getTemplateString(plugin_template, map)
  }
}
