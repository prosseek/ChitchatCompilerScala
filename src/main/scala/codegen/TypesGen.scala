package codegen

import node.TypeNode

import scala.collection.mutable.ListBuffer

class TypesGen(val typeNodes:List[TypeNode]) extends Gen {
  def gen() = {
    typeNodes foreach { tn =>
      (new TypeGen(tn, typeNodes)).gen(tn.name)
    }
  }
}