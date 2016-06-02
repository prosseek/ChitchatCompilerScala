package visitor

import parser.ChitchatParser.FunctionContext

trait FunctionProcessor {
  def process(ctx:FunctionContext, o:ChitchatVisitor) = {
    null
  }
}
