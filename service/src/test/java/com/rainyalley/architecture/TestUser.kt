package com.rainyalley.architecture

import com.rainyalley.architecture.model.entity.User


fun printUser(u:User){
    println(u)
}

fun main(args:Array<String>){
    //printUser(User())

    val list = listOf(1,2,3,4,5)
    val redlam = {p:Int,n:Int -> p+n}

    val sum = list.filter { v -> v > 2 }.map { v -> v + 5 }.reduce(redlam)
    println(sum)
}