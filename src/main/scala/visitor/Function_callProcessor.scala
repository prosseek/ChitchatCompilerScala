package visitor

import node._
import parser.ChitchatParser.{ExpressionContext, Function_callContext}

import scala.collection.mutable.ListBuffer

/**
  * function_call: id args
  * args: '(' ( constant ','?)* ')' ;
  */
trait Function_callProcessor {
  def process(ctx:Function_callContext, o:ChitchatVisitor) = {
    val id = o.visit(ctx.id()).asInstanceOf[IdNode]
    val constants = o.visit(ctx.args()).asInstanceOf[ArgsNode].constants
    Function_callNode(name = ctx.getText(), id = id, constants = constants)
  }
}