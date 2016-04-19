package visitor

import parser.{ChitchatBaseVisitor, ChitchatParser}
import node._

/**
  * Created by smcho on 4/18/16.
  */
class ChitchatVisitor extends ChitchatBaseVisitor[Node] {
  var prognode: ProgNode = _

  override  def visitProg(ctx: ChitchatParser.ProgContext) = {
    prognode = new ProgNode()

    val it = ctx.children.iterator()
    while (it.hasNext) {
      val item = it.next()
      val res: Node = visit(item)
      prognode.add(res)
    }

    prognode
  }

  override def visitTypedef(ctx: ChitchatParser.TypedefContext) : Node = {
    val name = ctx.id().getText()
    val typenode = new TypeNode(name)

    typenode
  }

  override def visitExpressions(ctx: ChitchatParser.ExpressionsContext) : Node = {
    val expressions = new ExpressionsNode()

    val item = ctx.getChildCount()
    val it = ctx.children.iterator()
    while(it.hasNext) {
      val res = it.next()
      // res is comma separated assignemnt
      if (res.getText() != ',') {
        visit(res)
      }
    }
    expressions
  }

  override def visitExpression(ctx: ChitchatParser.ExpressionContext) : Node = {
    val expressions = new ExpressionNode()
    if (ctx.comparison() != null) {

    }
    else if (ctx.assignment() != null) {
      val assignemnt = ctx.assignment()
      val key = assignemnt.ID().getText()
      val pe = assignemnt.primary_expresion()
      val result = parse(pe)
    }
    else if (ctx.function_call() != null) {

    }
    else if (ctx.primary_expresion() != null) {

    }
    else {
      throw new RuntimeException(s"Error expression wrong ${ctx.getText()}")
    }
    null
  }

  def parse(ctx: ChitchatParser.Primary_expresionContext)= {
    var value:Any = 0
    if (ctx.ID() != null) {
      value = ctx.ID().getText()
    }
    else if (ctx.STRING() != null) {
      value = ctx.STRING().getText()
    }
    else if (ctx.constant() != null) {
      if (ctx.constant().FLOAT() != null) {
        value = ctx.constant().FLOAT().getText().toFloat
      }
      else {
        value = ctx.constant().INT().getText().toInt
      }
    }
    else {
      throw new RuntimeException(s"Error in Primary_expresionContext ${ctx.getText()}")
    }
    value
  }
}
