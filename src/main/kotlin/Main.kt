package org.example
class Calculator{

    fun sum(numberOne: Int = 0, numberTwo: Int = 0) =  numberOne + numberTwo

}

fun main() {
    val calculator = Calculator()

    while (true){
        val input = readln().split(" ")

        if (input.contains("")){

            continue
        }


        if(input.contains("/exit")){
            println("Bye!")
            break
        }

        val numbers = input.map { it.toInt() }

        when(numbers.size){
            1 -> println(calculator.sum(numbers[0]))
            2 -> println(calculator.sum(numbers[0], numbers[1]))
        }
    }
}