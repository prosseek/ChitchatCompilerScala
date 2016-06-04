package codegen

import node._

class SituationCodeGen(val situationNode:SituationNode = null,
                    val situationNodes:List[SituationNode]) extends CodeGen {

  def gen(schemaName:String) = {}

  def generate() = {
    if (situationNode != null)
      gen(situationNode.id).toString
    else
      throw new RuntimeException(s"No typedefNode defined")
  }
}
