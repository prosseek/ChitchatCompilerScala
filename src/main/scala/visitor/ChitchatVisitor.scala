package visitor

import parser.ChitchatBaseVisitor
import parser.ChitchatParser._
import node._

class ChitchatVisitor extends ChitchatBaseVisitor[Node]
  with TypedefProcessor
  with CorrelationProcessor
  with ExpressionProcessor
  with ExpressionsProcessor
  with SituationProcessor
  with SchemaProcessor
  with FunctionProcessor
  with CommandProcessor
  with Function_callProcessor
  with ComparisonProcessor
  with ValueProcessor
  with ListProcessor
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
  override def visitTypedef(ctx: TypedefContext) : TypedefNode = process(ctx, this)
  override def visitCorrelation(ctx: CorrelationContext) : CorrelationNode = process(ctx, this)
  override def visitSituation(ctx: SituationContext) : SituationNode = process(ctx, this)
  override def visitSchema(ctx: SchemaContext) : SchemaNode = process(ctx, this)
  override def visitFunction(ctx: FunctionContext) : FunctionNode = process(ctx, this)
  override def visitCommand(ctx: CommandContext) : CommandNode = process(ctx, this)

  override def visitAssignment(ctx: AssignmentContext) : AssignmentNode =
    AssignmentNode(name = ctx.getText(), ID = ctx.ID().getText(),
      expression = visit(ctx.expression()).asInstanceOf[ExpressionNode])
  override def visitComparison(ctx: ComparisonContext) : ComparisonNode = process(ctx, this)
  override def visitFunction_call(ctx:Function_callContext) : Function_callNode = process(ctx, this)
  override def visitList(ctx:ListContext) : ListNode = process(ctx, this)
  override def visitConstant(ctx:ConstantContext) : ConstantNode = ConstantNode(name = ctx.getText())
  override def visitId(ctx:IdContext) : IdNode = IdNode(name = ctx.getText())

  override def visitExpressions(ctx: ExpressionsContext) : ExpressionsNode = process(ctx, this)
  override def visitExpression(ctx: ExpressionContext) : ExpressionNode = process(ctx, this)
  override def visitValue(ctx:ValueContext) : ValueNode = process(ctx, this)

}
