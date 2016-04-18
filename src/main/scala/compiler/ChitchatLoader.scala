package compiler

import parser._
import node._

import scala.collection.mutable.{Map => MMap}

class ChitchatLoader extends ChitchatBaseListener {
  val types = MMap[String, TypeNode]()

  override def exitTypedef(ctx: ChitchatParser.TypedefContext) = {
    // retrieve values
    val typeName = ctx.id().getText()
    val annotation = ctx.annotation().getText()
    val parent_type = ctx.base_type()
    val parent = parent_type.id().getText()
    val assigns = parent_type.assigns()
    val assignMap = MMap[String, Any]()

    /*_*/
    for (i <- assigns.assign().toArray) {
      val key = i.asInstanceOf[parser.ChitchatParser.AssignContext].children.get(0).getText
      val value = i.asInstanceOf[parser.ChitchatParser.AssignContext].children.get(2).getText
      try {
        assignMap(key) = value.toInt
      }
      catch {
        case e:java.lang.NumberFormatException => {
          assignMap(key) = value.toFloat
        }
        case _ => throw new RuntimeException(s"Error in conversion ${value}")
      }
    }
    val tn = new TypeNode(typeName)
    tn.annotation = if (annotation == "+") Annotation.PUBLIC else Annotation.PRIVATE
    tn.assignMap = assignMap.toMap
    tn.parent = parent
    // set the type definition
    types(typeName) = tn
  }

  def report = {
    types
  }

  //class MyVisitor extends ChitchatBaseVisitor[Int] {
  //  override def visitProg(ctx: ChitchatParser.ProgContext) {
  //    println(ctx.getText())
  //  }
}