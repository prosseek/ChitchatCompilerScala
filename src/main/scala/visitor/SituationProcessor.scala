package visitor

import node.SituationNode
import parser.ChitchatParser.{GroupingContext, SituationContext}

/**
  * Created by smcho on 4/26/16.
  */
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
  def process(ctx:SituationContext) : SituationNode = {
    val sitnode = SituationNode(name = ctx.id().getText())
    sitnode
  }
}
