package org.example
class Calculator{

    fun sum(vararg values: Int): Int{
        var result = 0

        for (value in values){

            result += value
        }

        return result
    }

    fun subtract(vararg values: Int): Int {
        var result = 0

        for (value in values){

            result -= value
        }

        return result
    }

    fun definePlusOrMinus(symbols: List<String>): String{
        var result = ""

        if (symbols.isEmpty())
            return result

        result = symbols[0]

        for (index in 1 .. symbols.lastIndex){
            val prev = result
            val next = symbols[index]

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
            input.contains("/help")  -> println("The program calculates the sum of numbers")
            input.contains("/exit") -> break
            else -> {
                val values = input.split(" ")

                if(values.size == 1) {
                    println(values[0])
                    continue
                }

                var result = 0

                for (index in 1 .. values.lastIndex step 2){
                    val prev = result
                    val next = values[index + 1].toInt()
                    val symbol = values[index]

                    val numberPlus = symbol.filter { it == '+' }.length
                    val numberMinus = symbol.filter { it == '-' }.length

                    val isPlus = numberPlus >= numberMinus

                    result = when{
                        isPlus -> calculator.sum(prev, next)
                        else -> calculator.subtract(prev, next)
                    }
                }

                println(result)
            }
        }
    }

    println("Bye!")
}