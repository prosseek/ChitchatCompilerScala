package node.codegen

import node._

/**
  === function ===
   1. function name => label
   2. the number of parameter (n) makes the "return n"
   3. the parameters are accessed with $bp - N
   4. local variable makes additional code
   5. local variables are accessed with $bp + N

  ==== Example ====
  {{{
    F2:
    load $bp - 1
    load $bp - 2
    load $bp - 3
    iadd
    iadd
    return 3
  }}}
  */

class FunctionCodeGen (val functionNode:FunctionNode = null,
                      val progNode:ProgNode) extends CodeGen {
  def gen(functionNodeName:String) = {

  }

  def generate() = {
    if (functionNode != null)
      gen(functionNode.id).toString
    else
      throw new RuntimeException(s"No Node defined")
  }
}

