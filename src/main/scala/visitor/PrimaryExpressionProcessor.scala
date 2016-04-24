package visitor

import node.PrimaryExpressionNode
import parser.ChitchatParser

trait PrimaryExpressionProcessor {
  /**
    * Returns a PrimaryExpressionNode with
    *   valueType = ID|STRING|FLOAT|INT|BOOLEAN
    *   value = Any
    *
    * @param ctx the parse tree
    *
    */

  def process(ctx: ChitchatParser.Primary_expresionContext) = {
    var value:String = ""
    var valueType:String = ""

    if (ctx.ID() != null) {
      value = ctx.ID().getText()
      valueType = "ID"
    }
    else if (ctx.STRING() != null) {
      value = ctx.STRING().getText()
      valueType = "STRING"
    }
    else if (ctx.constant() != null) {
      if (ctx.constant().CHAR() != null) {
        value = ctx.constant().CHAR().getText()
        valueType = "CHAR"
      }
      else if (ctx.constant().FLOAT() != null) {
        value = ctx.constant().FLOAT().getText()
        valueType = "FLOAT"
      }
      else if (ctx.constant().INT() != null) {
        value = ctx.constant().INT().getText()
        valueType = "INT"
      }
      else if (ctx.constant().TRUE() != null) {
        value = ctx.constant().TRUE().getText()
        valueType = "BOOLEAN"
      }
      else if (ctx.constant().FALSE() != null) {
        value = ctx.constant().FALSE().getText()
        valueType = "BOOLEAN"
      }
      else
        throw new RuntimeException(s"wrong primary expression ${ctx.toString}")
    }
    else {
      throw new RuntimeException(s"Error in Primary_expresionContext ${ctx.getText()}")
    }
    PrimaryExpressionNode(valueType = valueType, value = value)
  }
}
