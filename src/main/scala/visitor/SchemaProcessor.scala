package visitor

import node._
import parser.ChitchatParser.{ExpressionsContext, SchemaContext}
import scala.collection.mutable.ListBuffer

trait SchemaProcessor {
  def process(ctx: SchemaContext, o: ChitchatVisitor): SchemaNode = {

    val it = ctx.children.iterator()
    val es = ListBuffer[ExpressionsNode]()
    while (it.hasNext()) {
      val n = it.next()
      if (n.isInstanceOf[ExpressionsContext]) {
        val res = o.visit(n.asInstanceOf[ExpressionsContext]).asInstanceOf[ExpressionsNode]
        es += res
      }
    }

    SchemaNode(name = ctx.getText(),
      annotation = ctx.annotation().getText(),
      id = o.visit(ctx.id()).asInstanceOf[IdNode],
      expressions = es.toList)
  }
}
