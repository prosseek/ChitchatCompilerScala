package visitor

import parser.{ChitchatBaseVisitor, ChitchatParser}
import node._

import scala.collection.mutable.ListBuffer

class ChitchatVisitor extends ChitchatBaseVisitor[Node]
  with ExpressionProcessor
  with GroupingProcessor
  with PrimaryExpressionProcessor
{
  var prognode: ProgNode = _

  /** Processes defintions, and return a set of nodes
    * {{{
    *   prog: (typedef | correlation | situation | ... )+ ;
    * }}}
    *
    * @param ctx the parse tree
    *
    */
  override  def visitProg(ctx: ChitchatParser.ProgContext) = {
    prognode = new ProgNode()

    /* ctx has corresponding typedef(), correlation(), ...
       which can be null (when there is no input matching) or corresponding ctx object
       in this example, we iterate over the children to retrieve the item as an object
       and visit them one by one with a visitor.
     */
    val it = ctx.children.iterator()
    while (it.hasNext) {
      val item = it.next()
      val res: Node = visit(item)
      prognode.add(res)
    }
    prognode
  }


  /**
    * {{{
    *   typedef: annotation TYPE id EXT  base_type ;
    *   base_type: id '(' expressions ')' ;
    * }}}
    *
    * @param ctx the parse tree
    */
  override def visitTypedef(ctx: ChitchatParser.TypedefContext) : TypeNode = {
    val basetype = ctx.base_type()
    val annotation = ctx.annotation().getText()
    val typenode = TypeNode(name = ctx.id().getText().replace("\"", ""),
      annotation = annotation,
      base_name = basetype.id().getText().replace("\"", ""))
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
    * ==== Grammar ====
    * // correlation
    * {{{
    *   correlation: CORRELATION id '=' (grouping | from_group_name) ;
    *   from_group_name: 'schema.'  id ;
    *   grouping: '(' group_ids ')' ;
    *   group_ids: ((ID | STRING | grouping) ','?)+ ;
    * }}}
    *
    * @param ctx the parse tree
    *     */
  override def visitCorrelation(ctx: ChitchatParser.CorrelationContext) : CorrelationNode = {
    val cornode = CorrelationNode(name = ctx.id().getText())
    if (ctx.grouping() != null) {
      cornode.add(process(ctx.grouping()).toList)
    }
    else if (ctx.from_group_name() != null) {

    }
    cornode
  }

  override def visitExpression(ctx: ChitchatParser.ExpressionContext) : Node = {
    // **this** is necessary because process requires PrimaryExpressionProcessor
    process(ctx, this)
  }

  override def visitPrimary_expresion(ctx: ChitchatParser.Primary_expresionContext) = {
    process(ctx)
  }
}
