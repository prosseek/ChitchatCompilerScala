package codegen

import node._

class CommandCodeGen (val commandNode:CommandNode = null,
                      val commandNodes:List[CommandNode]) extends CodeGen {

  def gen(schemaName:String) = {}

  def generate() = {
    if (commandNode != null)
      gen(commandNode.name).toString
    else
      throw new RuntimeException(s"No valueNode defined")
  }
}
