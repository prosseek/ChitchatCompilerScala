package visitor

import node._
import parser.ChitchatParser.ExpressionContext

import scala.collection.mutable.ListBuffer

trait ExpressionProcessor  {
  /**
    * From the expression grammar create ExpressionNode
    * {{{
    *     expression: function_call | value | assignment | comparison ;
    * }}}
    *
    */

  def process(ctx: ExpressionContext, o:ChitchatVisitor) : ExpressionNode = {
    var result:Node = null
    if (ctx.comparison() != null) {
      result = o.visit(ctx.comparison())
    }
    else if (ctx.assignment() != null) {
      result = o.visit(ctx.assignment())
    }
    else if (ctx.function_call() != null) {
      result = o.visit(ctx.function_call())
    }
    else if (ctx.value() != null) {
      result = o.visit(ctx.value())
    }
    else {
      throw new RuntimeException(s"Error expression wrong ${ctx.getText()}")
    }
    ExpressionNode(name = ctx.getText(), node = result)
  }
}
