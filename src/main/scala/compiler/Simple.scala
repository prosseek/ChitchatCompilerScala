package compiler

import org.antlr.v4.runtime.tree.{ParseTree, ParseTreeWalker}
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import parser.{ChitchatBaseListener, ChitchatLexer, ChitchatParser}
import java.io.File
import java.io.FileInputStream
import java.io.IOException;

object Simple extends App {

  var filePath = "./resources/example/input.txt"
  var fileInput = new File(filePath);
  var fileInputStream = new FileInputStream(fileInput)

  val input: ANTLRInputStream = new ANTLRInputStream(fileInputStream)
  val lexer: ChitchatLexer = new ChitchatLexer(input)
  val tokens: CommonTokenStream = new CommonTokenStream(lexer)
  val parser: ChitchatParser = new ChitchatParser(tokens)
  val tree: ParseTree = parser.prog

  val walker: ParseTreeWalker = new ParseTreeWalker
  val loader: ChitchatLoader = new ChitchatLoader
  walker.walk(loader, tree)

  val types = loader.report
  println(types.mkString("{",";","}"))
  //val v = new MyVisitor
  //v.visit(tree)
  System.out.println(tree.toStringTree(parser))
}
