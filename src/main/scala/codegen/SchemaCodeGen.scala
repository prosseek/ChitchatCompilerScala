package codegen

import node._

class SchemaCodeGen(val schemaNode:CorrelationNode = null,
                  val schemaNodes:List[SchemaNode]) extends CodeGen {

  def gen(schemaName:String) = {}

  def generate() = {
    if (schemaNode != null)
      gen(schemaNode.id).toString
    else
      throw new RuntimeException(s"No typedefNode defined")
  }
}
