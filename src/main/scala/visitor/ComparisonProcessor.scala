package visitor

import node._
//import parser.ChitchatParser.ComparisonContext
//
///**
//  * comparison: '(' expression comparison_operator expression ')' ;
//  */
//trait ComparisonProcessor {
//  def process(ctx: ComparisonContext, o:ChitchatVisitor) : ComparisonNode = {
//
//    val id:IdNode = o.visit(ctx.id).asInstanceOf[IdNode]
//    val expression:ExpressionNode = o.visit(ctx.expression).asInstanceOf[ExpressionNode]
//
//    // comparison_operator: '<'|'>'|'<='|'>=';
//    val operator = ctx.comparison_operator().getText()
//
//    ComparisonNode(name = ctx.getText(), id = id, expression = expression, op = operator)
//  }
//}
