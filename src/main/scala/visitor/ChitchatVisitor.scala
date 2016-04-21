package visitor

import parser.{ChitchatBaseVisitor, ChitchatParser}
import node._

import scala.collection.mutable.ListBuffer

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
    val typenode = TypeNode(name = ctx.id().getText().replace("\"", ""),
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
      val function_call = ctx.function_call()
      val ID = function_call.ID().getText()
      val pe = function_call.params()

      val params = ListBuffer[Any]()

      if (pe.children != null) {
        val it = pe.children.iterator()

        while (it.hasNext) {
          params += it.next().getText()
        }
      }

      expression = FunctionCallNode(ID = ID, params = params.toSeq)
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
    *   valueType = ID|STRING|FLOAT|INT|BOOLEAN
    *   value = Any
    *
    * @param ctx the parse tree
    *     */
  override def visitPrimary_expresion(ctx: ChitchatParser.Primary_expresionContext) = {
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
