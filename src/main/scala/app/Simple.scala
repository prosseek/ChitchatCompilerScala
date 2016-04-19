package app

import java.io.{File, FileInputStream}

import codegen.TypeGen
import org.antlr.v4.runtime.tree.{ParseTree, ParseTreeWalker}
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import parser.{ChitchatLexer, ChitchatParser}
import visitor.ChitchatVisitor;

object Simple extends App {

  var filePath = "./resources/example/input.txt"
  var fileInput = new File(filePath)
  var fileInputStream = new FileInputStream(fileInput)

  val input: ANTLRInputStream = new ANTLRInputStream(fileInputStream)
  val lexer: ChitchatLexer = new ChitchatLexer(input)
  val tokens: CommonTokenStream = new CommonTokenStream(lexer)
  val parser: ChitchatParser = new ChitchatParser(tokens)
  val tree: ParseTree = parser.prog

  val walker = new ParseTreeWalker
  val visitor = new ChitchatVisitor
  visitor.visit(tree)

  val tg = new TypeGen(visitor.prognode.types)
  tg.gen()

  //val types = loader.report
  //println(types.mkString("{",";","}"))
  //val v = new MyVisitor
  //v.visit(tree)
  System.out.println(tree.toStringTree(parser))
}
