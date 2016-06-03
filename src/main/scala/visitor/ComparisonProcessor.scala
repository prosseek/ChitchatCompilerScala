package visitor

import node.{ComparisonNode, ExpressionNode, ExpressionsNode}
import parser.ChitchatParser.ComparisonContext

/**
  * comparison: '(' expression comparison_operator expression ')' ;
  */
trait ComparisonProcessor {
  def process(ctx: ComparisonContext, o:ChitchatVisitor) : ComparisonNode = {

    val expression1:ExpressionNode = o.visit(ctx.expression(0)).asInstanceOf[ExpressionNode]
    val expression2:ExpressionNode = o.visit(ctx.expression(1)).asInstanceOf[ExpressionNode]

    // comparison_operator: '<'|'>'|'<='|'>=';
    val operator = ctx.comparison_operator().getText()

    ComparisonNode(name = ctx.getText(), expression1 = expression1, expression2 = expression2, op = operator)
  }
}
