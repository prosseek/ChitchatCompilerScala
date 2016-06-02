package visitor

import node.{SchemaNode, SituationNode}
import org.antlr.v4.runtime.ParserRuleContext
import parser.ChitchatParser.{SchemaContext, SituationContext}

/**
  * Created by smcho on 6/2/16.
  */
trait SchemaProcessor {
  def process(ctx: SchemaContext, o: ChitchatVisitor): SchemaNode = {
    null
  }
}
