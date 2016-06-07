package node

import node.codegen.Template

/*
  expression: function_call | value | assignment
          | '(' expression ')'
          | expression comparison_operator expression  // comparsion
          | expression logic_operator expression ;     // logic
 */

case class ExpressionNode(override val name:String, val node:Node) extends Node(name = name) with Template {
  def codeGen(progNode:ProgNode) :String = {
    node match {
      case Function_callNode(name, id, constants) => node.codeGen(progNode)
      case AssignmentNode(name, id, expression) => node.codeGen(progNode)
      case ValueNode(name, node) => node.codeGen(progNode)
      case ComparisonNode(name, expression1, op, expression2) => node.codeGen(progNode)
      case LogicNode(name, expression1, op, expression2) => node.codeGen(progNode)
      case ArithmeticNode(name, expression1, op, expression2) => node.codeGen(progNode)
      case AbsoluteNode(name, expression1, op, expression2) => node.codeGen(progNode)
      case _ => throw new RuntimeException(s"Wrong Expression Node ${node}")
    }
  }
}

