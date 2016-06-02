package visitor

import parser.ChitchatBaseVisitor
import parser.ChitchatParser._
import node._

class ChitchatVisitor extends ChitchatBaseVisitor[Node]
  with TypedefProcessor
  with CorrelationProcessor
  with ExpressionProcessor
  with SituationProcessor
  with SchemaProcessor
  with FunctionProcessor
  with CommandProcessor
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
  override  def visitProg(ctx: ProgContext) = {
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

  // prog's children visitors
  override def visitTypedef(ctx: TypedefContext) : TypeNode = process(ctx, this)
  override def visitCorrelation(ctx: CorrelationContext) : CorrelationNode = process(ctx, this)
  override def visitSituation(ctx: SituationContext) : SituationNode = process(ctx, this)
  override def visitSchema(ctx: SchemaContext) : SchemaNode = process(ctx, this)
  override def visitFunction(ctx: FunctionContext) : FunctionNode = process(ctx, this)
  override def visitCommand(ctx: CommandContext) : CommandNode = process(ctx, this)

  override def visitExpression(ctx: ExpressionContext) : ExpressionNode = process(ctx, this)
}
