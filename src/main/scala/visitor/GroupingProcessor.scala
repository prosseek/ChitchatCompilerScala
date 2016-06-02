package visitor

import parser.ChitchatParser
import collection.mutable.{Set => MSet}
/**
  * Processes the group_ids
  * {{{
  * grouping: '(' group_ids ')' ;
  * group_ids: ((ID | STRING | grouping) ','?)+ ;
  * }}}
  *
  */
trait GroupingProcessor {
/*
  def process(ctx:GroupingContext) : Set[String] = {
    val nameSet = MSet[String]()
    val group_ids = ctx.group_ids()
    val it = group_ids.children.iterator()

    while(it.hasNext()) {
      val result = it.next()
      if (result.isInstanceOf[ChitchatParser.GroupingContext]) {
        nameSet ++= process(result.asInstanceOf[ChitchatParser.GroupingContext])
      }
      else if (result.getText() != ",") {
        nameSet += result.getText()
      }
    }
    nameSet.toSet
  }
  */
}
