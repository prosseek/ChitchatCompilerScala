package visitor

import node._
import parser.ChitchatParser.TypedefContext

/**
  * {{{
  *   typedef: annotation TYPE id EXT  base_type ;
  *   base_type: id '(' expressions ')' ;
  * }}}
  *
  */
trait TypedefProcessor {
  def process(ctx: TypedefContext, o:ChitchatVisitor) : TypedefNode = {
    // get Contexts
    val basetype = ctx.base_type()
    val annotation = ctx.annotation().getText()
    val basename = o.visit(basetype.id()).asInstanceOf[IdNode].name.replace("\"", "")

    // todo: CLARIFY
    // id: ID | STRING;
    // string's \" should be removed to be used as a name
    val idNode = o.visit(ctx.id()).asInstanceOf[IdNode].name
    val newIdNode = IdNode(name = idNode.replace("\"", ""))
    // create node
    val typenode = TypedefNode(
      name = ctx.getText(),
      id = newIdNode,
      annotation = annotation,
      base_name = basename)

    // type has multiple expressions
    // add only expressions with assignment, value, or function call
    // +type time extends Encoding(hour, minute)
    // +type temperature extends Float(min=-50.0, max=90.0)
    // +type max10 extends String(maxlength(10))
    val it = basetype.expressions().children.iterator()
    while(it.hasNext) {
      val res = it.next()
      // expressions: (expression ','?)+;
      // expression: function_call | value | assignment | comparison (not used!) ;
      // res is comma separated assignemnt
      if (res.getText() != ",") {
        val node = o.visit(res).asInstanceOf[ExpressionNode].node
        if      (node.isInstanceOf[AssignmentNode]) typenode.add(node.asInstanceOf[AssignmentNode])
        else if (node.isInstanceOf[Function_callNode]) typenode.add(node.asInstanceOf[Function_callNode])
        else if (node.isInstanceOf[ValueNode]) typenode.add(node.asInstanceOf[ValueNode])
        else if (node.isInstanceOf[ComparisonNode]) typenode.add(node.asInstanceOf[ComparisonNode])
        else {
          throw new RuntimeException(s"wrong expression in extended types ${res.getText()}")
        }
      }
    }
    typenode
  }
}
