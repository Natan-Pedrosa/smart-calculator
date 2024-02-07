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
            input == "/exit" -> break
            input == "/help" -> println("The program calculates the sum of numbers")
            else -> {
                val values = input.split(" ").map { it.toInt() }

                calculator.sum(values)
            }
        }
    }

    println("Bye!")
}