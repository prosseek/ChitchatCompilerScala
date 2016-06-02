package visitor

import node.{ExpressionNode, TypeNode}
import parser.ChitchatParser.TypedefContext

/**
  * {{{
  *   typedef: annotation TYPE id EXT  base_type ;
  *   base_type: id '(' expressions ')' ;
  * }}}
  *
  * @param ctx the parse tree
  */
trait TypedefProcessor {
  def process(ctx: TypedefContext, o:ChitchatVisitor) : TypeNode = {
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
        typenode.add(o.visit(res).asInstanceOf[ExpressionNode])
      }
    }
    typenode
  }
}
