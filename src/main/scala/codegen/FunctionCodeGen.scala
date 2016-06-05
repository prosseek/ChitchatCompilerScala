package codegen

import node._

class FunctionCodeGen (val functionNode:FunctionNode = null,
                      val situationNodes:List[SituationNode]) extends CodeGen {
  def gen(functionNodeName:String) = {

  }

  def generate() = {
    if (functionNode != null)
      gen(functionNode.id).toString
    else
      throw new RuntimeException(s"No Node defined")
  }
}

