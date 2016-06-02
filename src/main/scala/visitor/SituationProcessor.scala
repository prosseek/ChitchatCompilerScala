package visitor

import node.SituationNode
import org.antlr.v4.runtime.ParserRuleContext
import parser.ChitchatParser.SituationContext

trait SituationProcessor {
  /**
    *
    * ==== Grammar ====
    * {{{
    *   situation: SITUATION id parenparams? '=' constraints ;
    *   parenparams: '(' params ')' ;
    *   params: (primary_expresion ','?)* ;
    *   constraints: absolute_constraint | range_constraint ;
    *   absolute_constraint: '|' id '-' id '|' comparison_operator unit_value  ;
    *   range_constraint: unit_value comparison_operator id comparison_operator unit_value ;
    * }}}
    *
    * @param ctx the parse tree
    *     */
  // def process(ctx:SituationContext, o:ChitchatVisitor) : SituationNode = {
  def process(ctx:SituationContext, o:ChitchatVisitor) : SituationNode = {
    //val sitnode = SituationNode(name = ctx.id().getText())
    //sitnode
    null
  }
}
