package visitor

import node.CorrelationNode
import org.antlr.v4.runtime.ParserRuleContext
import parser.ChitchatParser
import parser.ChitchatParser.CorrelationContext

/**
  * ==== Grammar ====
  * // correlation
  * {{{
  *   correlation: CORRELATION id '=' (grouping | from_group_name) ;
  *   from_group_name: 'schema.'  id ;
  *   grouping: '(' group_ids ')' ;
  *   group_ids: ((ID | STRING | grouping) ','?)+ ;
  * }}}
  *
  */
trait CorrelationProcessor {
  def process(ctx: CorrelationContext, o:ChitchatVisitor) : CorrelationNode = {
    val cornode = CorrelationNode(name = ctx.id().getText())
    cornode
  }
}
