package visitor

import node._
import parser.ChitchatParser.{ExpressionContext, Function_callContext}

import scala.collection.mutable.ListBuffer

/**
  * function_call: ID '(' expressions ')' ;
  */
trait Function_callProcessor {
  def process(ctx:Function_callContext, o:ChitchatVisitor) = {
    val id = ctx.ID().getText()
    val params = o.visit(ctx.expressions()).asInstanceOf[ExpressionsNode].expressions
    Function_callNode(name = ctx.getText(), ID = id, params = params)
  }
}