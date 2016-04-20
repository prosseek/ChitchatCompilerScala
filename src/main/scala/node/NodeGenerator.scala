package node

import java.io.{File, FileInputStream}

import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import parser.{ChitchatLexer, ChitchatParser}
import visitor.ChitchatVisitor

object NodeGenerator {
  def get(filePath:String) = {
    //var filePath = "./resources/example/input.txt"
    var fileInput = new File(filePath)
    var fileInputStream = new FileInputStream(fileInput)

    val input: ANTLRInputStream = new ANTLRInputStream(fileInputStream)
    val lexer: ChitchatLexer = new ChitchatLexer(input)
    val tokens: CommonTokenStream = new CommonTokenStream(lexer)
    val parser: ChitchatParser = new ChitchatParser(tokens)
    val tree = parser.prog
    val visitor = new ChitchatVisitor
    visitor.visit(tree)
    visitor.prognode
  }
}
