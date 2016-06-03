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
    val params = ListBuffer[ExpressionNode]()

    val it = ctx.expressions().children.iterator()

    while (it.hasNext()) {
      val item = it.next()
      if (item.isInstanceOf[ExpressionContext]) {
        params += o.visit(item.asInstanceOf[ExpressionContext]).asInstanceOf[ExpressionNode]
      }
    }

    Function_callNode(ID = id, params = params.toList)
  }
}