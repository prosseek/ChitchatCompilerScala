package visitor

import node._
import parser.ChitchatParser._

import scala.collection.mutable.ListBuffer

/**
  * list: '[' (constant ','?)+ ']' ;
  */
trait ListProcessor {
  def process(ctx: ListContext, o:ChitchatVisitor) : ListNode = {
    val result = ListBuffer[ConstantNode]()
    val it = ctx.children.iterator()
    while(it.hasNext()) {
      val item = it.next()
      if (item.isInstanceOf[ConstantContext]) {
        // todo: not fully tested
        result += o.visit(item.asInstanceOf[ConstantContext]).asInstanceOf[ConstantNode]
      }
    }
    ListNode(name = ctx.getText(), values = result.toList)
  }
}
