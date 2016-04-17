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

    val tn = new TypeNode(typeName)
    tn.annotation = if (annotation == "+") Annotation.PUBLIC else Annotation.PRIVATE

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