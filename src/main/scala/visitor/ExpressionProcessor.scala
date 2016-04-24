package visitor

import node._
import parser.ChitchatParser.ExpressionContext

import scala.collection.mutable.ListBuffer

trait ExpressionProcessor {
  var expression: ExpressionNode = null

  /**
    * From the expression grammar create ExpressionNode
    * {{{
    * expression: comparison | assignment | function_call | primary_expresion;
    * }}}
    *
    * @param ctx
    * @param o
    * @return
    */

  def process(ctx: ExpressionContext, o:ChitchatVisitor) = {
    if (ctx.comparison() != null) {
      val node = o.visit(ctx.comparison().primary_expresion())
      expression = ComparisonNode(ID = ctx.comparison().ID().getText(),
        op = ctx.comparison().comparison_operator().getText(),
        node = node.asInstanceOf[PrimaryExpressionNode])
    }
    else if (ctx.assignment() != null) {
      val assignemnt = ctx.assignment()
      val ID = assignemnt.ID().getText()
      val pe = assignemnt.primary_expresion()
      val node = o.visit(pe)
      expression = AssignNode(ID = ID, node = node.asInstanceOf[PrimaryExpressionNode])
    }
    else if (ctx.function_call() != null) {
      val function_call = ctx.function_call()
      val ID = function_call.ID().getText()
      val pe = function_call.params()

      val params = ListBuffer[Any]()

      if (pe.children != null) {
        val it = pe.children.iterator()

        while (it.hasNext) {
          params += it.next().getText()
        }
      }
      expression = FunctionCallNode(ID = ID, params = params.toSeq)
    }
    // primary expression requires its own processing
    else if (ctx.primary_expresion() != null) {
      expression = o.visit(ctx.primary_expresion()).asInstanceOf[PrimaryExpressionNode]
    }
    else {
      throw new RuntimeException(s"Error expression wrong ${ctx.getText()}")
    }
    expression
  }
}
