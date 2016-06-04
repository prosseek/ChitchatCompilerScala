package codegen

import node.CorrelationNode

class CorrelationCodeGen(val correlationNode:CorrelationNode = null, val correlationNodes:List[CorrelationNode])  extends CodeGen {

  def gen(correlationNodeName:String) = {}

  def generate() = {
    if (correlationNode != null)
      gen(correlationNode.id)
    else
      throw new RuntimeException(s"No typedefNode defined")
  }
}
