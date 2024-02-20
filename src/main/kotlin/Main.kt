import kotlin.math.pow

class Calculator{

    val memory = mutableMapOf<String, Int>()

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

    fun operation(firstValue: Int, secondValue: Int, operator: String): Int{
        return when(operator){
            "+" -> sum(secondValue, firstValue)
            "-" -> subtract(secondValue, firstValue)
            "*" -> firstValue * secondValue
            "/" -> secondValue / firstValue
            "^" -> secondValue.toDouble().pow(firstValue.toDouble()).toInt()
            else -> 0
        }
    }

    fun calculatePostFixExpression(expressionPostFix: List<String>): Int{
        val stack = mutableListOf<Int>()
        val operators = listOf("/", "*", "^", "+", "-")

        for (value in expressionPostFix){
            when{
                value.toIntOrNull() != null -> stack.add(value.toInt())
                memory.containsKey(value) -> stack.add(memory[value]!!.toInt())
                value in operators -> {
                    val firstValue = stack.removeLast()
                    val secondValue = stack.removeLast()

                    val result = operation(firstValue, secondValue, value)
                    stack.add(result)
                }
            }
        }

        return stack.last()
    }
}

fun isDigit(value: String): Boolean{
    return value.toIntOrNull() != null
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
            input.contains("=") -> {

                val array = input.filter{it != ' '}.split("=")

                if(array.size > 2){
                    println("Invalid assignment")
                    continue
                }

                val key = array[0]
                val value = array[1]

                if(isNoBasicLatinLetter(key)){
                    println("Invalid identifier")
                    continue
                }

                if(isDigit(value))
                    calculator.memory[key] = value.toInt()
                else{
                    if(isNoBasicLatinLetter(value)) {
                        println("Invalid identifier")
                        continue
                    }

                    if(calculator.memory.contains(value))
                            calculator.memory[key] = calculator.memory.getOrDefault(value, 0)
                        else
                            println("Unknown variable")
                    }
            }
            else -> {
                val values = input.split(" ").filter { it != "" }

                if(values.size == 1) {
                  when{
                      input.any{ it.isLetter()} -> {
                          if (isNoBasicLatinLetter(input)){
                              println("Invalid identifier")
                              continue
                          }

                          if(calculator.memory.contains(input))
                              println(calculator.memory[input])
                          else
                              println("Unknown variable")
                      }
                      input[0] == '+' -> println(input.subSequence(1, input.length))
                      !input[input.length - 1].isDigit() -> println("Invalid expression")
                      else -> println(input)
                  }
                  continue
                }

                try {

                    val postFixExpression = convertInFixToPostFixExpression(values)

                    println(calculator.calculatePostFixExpression(postFixExpression))
                } catch (e: Exception){
                    println("Invalid expression")
                }
            }
        }
    }

    println("Bye!")
}

fun isNoBasicLatinLetter(key: String): Boolean {
    return key.any { (it.code !in 65 .. 90) && (it.code !in 97 .. 122)}
}

fun isDigitOrVariable(value: String): Boolean{
    return value.any { it.isDigit() || it.isLetter() }
}

fun isIncomingOperatorWithHigherPrecedence(incomingOperator: String, topStackOperator: String): Boolean{
    val multipleOrDivision = mutableListOf("*", "/")

    return  when(incomingOperator){
        "^" -> incomingOperator != topStackOperator
        in multipleOrDivision -> topStackOperator !in multipleOrDivision
        else ->  false
    }
}
fun convertInFixToPostFixExpression(expression: List<String>): MutableList<String>{
    val stack = mutableListOf<String>()
    val postFixExpression = mutableListOf<String>()

    for(value in expression){
        when{
            isDigitOrVariable(value) -> postFixExpression.add(value)
            stack.isEmpty() || stack.last() == "(" -> stack.add(value)
            isIncomingOperatorWithHigherPrecedence(value, stack.last()) -> stack.add(value)
            value == stack.last() || !isIncomingOperatorWithHigherPrecedence(value, stack.last()) ->
                postFixExpression.add(stack.removeLast())
            value == "(" -> stack.add(value)
            value == ")" -> {
                while (stack.last() != "(") {
                    postFixExpression.add(stack.removeLast())
                }
                stack.removeLast()
            }
        }
    }

    while (stack.isNotEmpty())
        postFixExpression.add(stack.removeLast())

    return postFixExpression
}