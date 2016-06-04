package codegen

import node._

class ValueCodeGen (val valueNode:ValueNode = null,
                    val valueNodes:List[ValueNode]) extends CodeGen {

  def gen(schemaName:String) = {}

  def generate() = {
    if (valueNode != null)
      gen(valueNode.name).toString
    else
      throw new RuntimeException(s"No valueNode defined")
  }
}
