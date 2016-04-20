package visitor

import parser.{ChitchatBaseVisitor, ChitchatParser}
import node._

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
    val basetype = ctx.base_type()
    val annotation = ctx.annotation().getText()
    val typenode = TypeNode(name = ctx.id().getText(),
      annotation = annotation,
      base_name = basetype.id().getText())
    val it = basetype.expressions().children.iterator()
    while(it.hasNext) {
      val res = it.next()
      // res is comma separated assignemnt
      if (res.getText() != ",") {
        typenode.add(visit(res).asInstanceOf[ExpressionNode])
      }
    }

    typenode
  }

  /**
    * Returns one of the ExpressionNode
    *   ComparisonNode|AssignNode|PrimaryExpressionNode
    *
    * @param ctx the parse tree
    *     */
  override def visitExpression(ctx: ChitchatParser.ExpressionContext) : Node = {
    var expression: ExpressionNode = null

    if (ctx.comparison() != null) {
      val node = visit(ctx.comparison().primary_expresion())
      expression = ComparisonNode(ID = ctx.comparison().ID().getText(),
        op = ctx.comparison().comparison_operator().getText(),
        node = node.asInstanceOf[PrimaryExpressionNode])
    }
    else if (ctx.assignment() != null) {
      val assignemnt = ctx.assignment()
      val ID = assignemnt.ID().getText()
      val pe = assignemnt.primary_expresion()
      val node = visit(pe)
      expression = AssignNode(ID = ID, node = node.asInstanceOf[PrimaryExpressionNode])
    }
    else if (ctx.function_call() != null) {
      throw new RuntimeException("function call is not implemented")
    }
    else if (ctx.primary_expresion() != null) {
      expression = visit(ctx.primary_expresion()).asInstanceOf[PrimaryExpressionNode]
    }
    else {
      throw new RuntimeException(s"Error expression wrong ${ctx.getText()}")
    }
    expression
  }

  /**
    * Returns a PrimaryExpressionNode with
    *   valueType = ID|STRING|FLOAT|INT
    *   value = Any
    *
    * @param ctx the parse tree
    *     */
  override def visitPrimary_expresion(ctx: ChitchatParser.Primary_expresionContext) = {
    var value:Any = 0
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
      if (ctx.constant().FLOAT() != null) {
        value = ctx.constant().FLOAT().getText().toFloat
        valueType = "FLOAT"
      }
      else {
        value = ctx.constant().INT().getText().toInt
        valueType = "INT"
      }
    }
    else {
      throw new RuntimeException(s"Error in Primary_expresionContext ${ctx.getText()}")
    }
    PrimaryExpressionNode(valueType = valueType, value)
  }
}
