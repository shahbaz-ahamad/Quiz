package com.example.quiz.datamodel

data class QuestionCollection(
    val response_code: Int,
    val results: List<Result>
){
    constructor():this(0, listOf())
}