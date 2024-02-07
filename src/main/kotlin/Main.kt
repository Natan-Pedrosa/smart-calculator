package org.example
class Calculator{

    fun sum(values: List<Int>): Int{
        var result = 0

        for (value in values){

            result += value
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
                val values = input.split(" ").map { it.toInt() }

                println(calculator.sum(values))
            }
        }
    }

    println("Bye!")
}