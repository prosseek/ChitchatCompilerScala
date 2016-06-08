package node

/**
  * schema: annotation SCHEMA id '=' '(' (scheme ','?)+ ')' ;
  * scheme: id | rep;
  * rep: '(' (id ','?)+ ')' '+';
  *
  * @param name
  * @param id
  * @param annotation
  * @param schemes
  */
case class SchemaNode(override val name:String, override val id:IdNode, val annotation:String, val schemes:List[SchemeNode]) extends Node(name = name, id = id) {

  def codeGen(progNode:ProgNode) :String = {
    val s = new StringBuilder

    schemes foreach {
      schema => s ++= _codeGen(schema, progNode)
    }

    s.toString
  }

  /**
    * === Simple example ===
    * {{{
    *   # +schema sender = (a b c)
    *     read a
    *     jpeekfalse END
    *     register a
    *     read b
    *     jpeekfalse END
    *     register b
    *     read c
    *     jpeekfalse END
    *     register c
    *     push_summary
    *    END:
    *     stop
    * }}}
    *
    * @param progNode
    * @return
    */
  def _codeGen(es:SchemeNode, progNode:ProgNode) = {
    val endNode = id.name + "END"

    ""
  }
}
