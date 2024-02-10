
class Calculator{

    fun sum(vararg values: Int): Int{
        var result = 0

        for (value in values){

            result += value
        }

        return result
    }

    fun subtract(vararg values: Int): Int {
        var result = values[0]

        for (index in 1 .. values.lastIndex){
            val prev = result
            val next = values[index]

            result = prev - next
        }

        return result
    }

    fun definePlusOrMinus(symbols: String): String{
        var result = ""

        if (symbols.isEmpty())
            return result

        result = symbols[0].toString()

        for (index in 1 .. symbols.lastIndex){
            val prev = result
            val next = symbols[index].toString()

            val tempResult = prev + next
            result = when (tempResult) {
                "++" -> "+"
                "-+" -> "-"
                "+-" -> "-"
                "--" -> "+"
                else -> ""
            }
        }

        return result
    }
}


fun main() {
    val calculator = Calculator()

    while (true){
        val input = readln()

        when{
            input.isBlank() -> continue
            input.contains("/help")  -> println("The program calculates the sum  and subtraction of numbers. " +
                    "Also support unary and binary plus/minus operator")
            input.contains("/exit") -> break
            input.contains("/") -> println("Unknown command")
            else -> {
                val values = input.split(" ").filter { it != "" }

                if(values.size == 1) {
                  when{
                      input[0] == '+' -> println(input.subSequence(1, input.length))
                      !input[input.length - 1].isDigit() -> println("Invalid expression")
                      else -> println(input)
                  }
                  continue
                }

                try {
                    var result = values[0].toInt()

                    for (index in 1..values.lastIndex step 2) {
                        val prev = result
                        val symbol = calculator.definePlusOrMinus(values[index])
                        val next = values[index + 1].toInt()

                        result = when {
                            symbol == "+" -> calculator.sum(prev, next)
                            else -> calculator.subtract(prev, next)
                        }
                    }

                    println(result)
                } catch (e: Exception){
                    println("Invalid expression")
                }
            }
        }
    }

    println("Bye!")
}