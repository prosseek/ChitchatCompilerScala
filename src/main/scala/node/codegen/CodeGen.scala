package node.codegen

import node.Node

abstract class CodeGen {
  def getTemplateString(template:String, replacement:Map[String, String]) = {
    replacement.foldLeft(template)((s:String, x:(String,String)) => ( "#\\{" + x._1 + "\\}" ).r.replaceAllIn( s, x._2 ))
  }

  def getClassName(name:String) = {
    name.replace("\"","").capitalize.replace(" ", "_")
  }

  def generate() : String
}
