
import java.math.BigInteger

class Calculator{

    val memory = mutableMapOf<String, BigInteger>()

    private fun sum(vararg values: BigInteger): BigInteger{
        var result = BigInteger.ZERO

        for (value in values){

            result += value
        }

        return result
    }

    private fun subtract(vararg values: BigInteger): BigInteger {
        var result = values[0]

        for (index in 1 .. values.lastIndex){
            val prev = result
            val next = values[index]

            result = prev - next
        }
        return result
    }

    fun defineOperatorInSequence(symbols: String): String{

        val regexPlus = "\\++".toRegex()
        val regexMinus = "-+".toRegex()
        val regexMultiple = "\\*{2,}".toRegex()
        val regexDivision = "/{2,}}".toRegex()

        return when{
            symbols matches regexPlus -> "+"
            symbols matches regexMinus -> if(symbols.length % 2 == 0) "+" else "-"
            symbols matches regexMultiple -> "ERRO"
            symbols matches regexDivision -> "ERRO"
            else -> symbols
        }
    }

    private fun operation(firstValue: BigInteger, secondValue: BigInteger, operator: String): BigInteger{
        return when(operator){
            "+" -> sum(secondValue, firstValue)
            "-" -> subtract(secondValue, firstValue)
            "*" -> firstValue * secondValue
            "/" -> secondValue / firstValue
            "^" -> secondValue.pow(firstValue.toInt())
            else -> BigInteger.ZERO
        }
    }

    fun calculatePostFixExpression(expressionPostFix: List<String>): BigInteger{
        if(expressionPostFix.contains("(") || expressionPostFix.contains(")"))
            throw Exception()

        val stack = mutableListOf<BigInteger>()
        val operators = listOf("/", "*", "^", "+", "-")

        for (value in expressionPostFix){
            when{
                value.toBigIntegerOrNull() != null -> stack.add(value.toBigInteger())
                memory.containsKey(value) -> stack.add(memory[value]!!)
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

    fun convertInFixToPostFixExpression(expression: List<String>): MutableList<String>{
        val stack = mutableListOf<String>()
        val postFixExpression = mutableListOf<String>()

        for(value in expression){
            when{
                value == "(" -> stack.add(value)
                value == ")" -> {
                    while (stack.isNotEmpty() && stack.last() != "(") {
                        postFixExpression.add(stack.removeLast())
                    }

                    stack.removeLast()
                }
                isDigitOrVariable(value) -> postFixExpression.add(value)
                stack.isEmpty() || stack.last() == "(" -> stack.add(value)
                isIncomingOperatorWithHigherPrecedence(value, stack.last()) -> stack.add(value)
                value == stack.last() || !isIncomingOperatorWithHigherPrecedence(value, stack.last()) ->{
                    while(stack.isNotEmpty() && stack.last() != "(" && !isIncomingOperatorWithHigherPrecedence(value, stack.last()) ){
                        postFixExpression.add(stack.removeLast())
                    }

                    stack.add(value)
                }
            }
        }

        while (stack.isNotEmpty())
            postFixExpression.add(stack.removeLast())

        return postFixExpression
    }
}

fun isDigit(value: String): Boolean{
    return value.toBigIntegerOrNull() != null
}

fun main() {
    val calculator = Calculator()

    while (true){
        var input = readln()

        when{
            input.isBlank() -> continue
            input.contains("/help")  -> println("The program calculates the sum  and subtraction of numbers. " +
                    "Also support unary and binary plus/minus operator")
            input.contains("/exit") -> break
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
                    calculator.memory[key] = value.toBigInteger()
                else{
                    if(isNoBasicLatinLetter(value)) {
                        println("Invalid identifier")
                        continue
                    }

                    if(calculator.memory.contains(value))
                        calculator.memory[key] = calculator.memory[value] ?: BigInteger.ZERO
                    else
                        println("Unknown variable")
                }
            }
            else -> {
                if(input[0] == '/'){
                    println("Unknown command")
                    continue
                }

                val values = getExpression(input)

                if(values.contains("ERRO")){
                    println("Invalid expression")
                    continue
                }

                if(values.size == 1) {
                    input = input.trim()
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
                    values.map{
                        if( it[0] in "/*-+")
                            calculator.defineOperatorInSequence(it)
                    }

                    val postFixExpression = calculator.convertInFixToPostFixExpression(values)

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

fun getExpression(expression: String): List<String>{

    var newExpression = expression.replace("--".toRegex(), "+")
    newExpression = newExpression.replace("\\++".toRegex(), "+")
    newExpression = newExpression.replace("\\+-".toRegex(), "-")
    newExpression = newExpression.replace("\\*[2,]".toRegex(), "ERRO")
    newExpression = newExpression.replace("/[2,]".toRegex(), "ERRO")

    val tempValues = newExpression.split("").filter { it != " " && it != "" }

    val values = mutableListOf<String>()
    var tempValue = ""

    for(value in tempValues){
        when{
            value in "*/+-^()" -> {

                if (tempValue != "")
                    values.add(tempValue)

                values.add(value)

                tempValue = ""
            }
            else -> tempValue += value
        }
    }

    if(tempValue.isNotEmpty())
        values.add(tempValue)

    return values.toList()
}

