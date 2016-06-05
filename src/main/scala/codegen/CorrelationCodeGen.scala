package codegen

import node.CorrelationNode

/*
  == Examples ==

  === Simple case ===
  {{{
  # correlation location = (latitude, longitude)
  # correlation bookseller = (name, location)
  }}}

  ==== Generated code ====
  {{{
    allexists string age longitude latitude date time
    stop
  }}}

  === Function ===

  ====
  # correlation producePrice = (priceMatch(produceName, price))
  # function bool priceMatch(produceName, price) = {
  #  return ( produceName == "apple" && 0 <= price <= 1000 )
  #  produceName == "apple" && price <= 1000 && price >= 0)
  # }

  ==== Generated code ====
   {{{

        # first
            read producename
            jpeekfalse END
            read price_i
            jpeekfalse END
            function_call_stack priceMatch 2
        END:
            stop
   }}}

   This portion is from the function implementation

   {{{
        priceMatch:
            load $bp - 2
            push "apple"
            cmp

            load $bp - 1
            push 1000
            le

            load $bp - 1
            push 0
            ge

            and 3
            return 2

    }}}
 */

/**
  *
  * @param correlationNode
  * @param correlationNodes
  */
class CorrelationCodeGen(val correlationNode:CorrelationNode, val correlationNodes:List[CorrelationNode])  extends CodeGen {

  def generate_for_simple(values:List[String]) = {
    val res = new StringBuilder
    res ++= values.mkString("allexists ", " ", "\n")
    res ++= "stop\n"
    res.toString
  }

  def generate_for_function() = {

  }

  def generate() : String = {
    if (correlationNode != null) {
      if (correlationNode.function_call == null) {
        val info = correlationNode.get(correlationNodes)
        return generate_for_simple(info)
      }
      else {
        ""
      }
    }
    else
      throw new RuntimeException(s"No correlationNode defined")
  }
}
