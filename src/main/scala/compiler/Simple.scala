package compiler

import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import parser.{ChitchatLexer, ChitchatParser}

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
  * Created by smcho on 4/16/16.
  */
object Simple extends App {

  var filePath = "./resources/example/input.txt"
  var fileInput = new File(filePath);
  var fileInputStream = new FileInputStream(fileInput)

  val input: ANTLRInputStream = new ANTLRInputStream(fileInputStream)
  val lexer: ChitchatLexer = new ChitchatLexer(input)
  val tokens: CommonTokenStream = new CommonTokenStream(lexer)
  val parser: ChitchatParser = new ChitchatParser(tokens)
  val tree: ParseTree = parser.prog
  System.out.println(tree.toStringTree(parser))
}
