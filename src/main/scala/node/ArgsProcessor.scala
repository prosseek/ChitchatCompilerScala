package visitor

import node._
import parser.ChitchatParser._

import scala.collection.mutable.ListBuffer

trait ArgsProcessor {
  def process(ctx: ArgsContext, o:ChitchatVisitor) : ArgsNode = {
    val constants = ListBuffer[ConstantNode]()
    val it = ctx.children.iterator()

    while (it.hasNext()) {
      val item = it.next()
      if (item.isInstanceOf[ConstantContext]) {
        constants += o.visit(item.asInstanceOf[ConstantContext]).asInstanceOf[ConstantNode]
      }
    }
    ArgsNode(name = ctx.getText(), constants = constants.toList)
  }
}
