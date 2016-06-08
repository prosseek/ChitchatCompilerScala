package node

import node.codegen.Template
import scala.collection.mutable.{Map => MMap}

/**
  * choose: (id '|'?)+ ;
  */
case class ChooseNode(override val name:String,
                      val ids:List[IdNode]) extends Node(name = name) with Template {
  /*
    read event
    jpeekfalse EVENT
    register event
    jmp ADVERTISEMENTEND
EVENT:

    pop $temp
    read advertisement
    jpeekfalse END
    register advertisement
ADVERTISEMENTEND:
   */

  def codeGen(progNode:ProgNode, labels:Map[String, String] = null) :String = {

    def getContent() = {
      val endLabel = labels("endLabel")
      val res = new StringBuilder
      ids foreach {
        id => res ++= s"read ${id.name}\njpeekfalse ${id.name}_LABEL:\nregister${id.name}\njmp ${endLabel}"
      }

      res.toString
    }

    def getLastContent() = {
      ""
    }

    val map = MMap[String, String]()
    map("labelend") = name + "_END"
    map("content") = getContent()
    map("lastContent") = getLastContent()
    val template =
      """#{content}
        |#{lastContent}
        |#{labelend}:
      """.stripMargin

    getTemplateString(template, map.toMap)
  }
}
