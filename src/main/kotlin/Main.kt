package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val (numberOne, numberTwo) = readln().split(" ").map{ it.toInt() }

    println(sum(numberOne.toInt(), numberTwo.toInt()))
}
fun sum(valueOne: Int, valueTwo: Int) = valueOne + valueTwo