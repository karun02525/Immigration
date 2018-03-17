package com.immigration.view.questions.model

import com.google.gson.annotations.SerializedName


data class QuestionModel(
		@SerializedName("message") val message: String = "",
		@SerializedName("result") val result: List<Result> = listOf()
)

data class Result(
		@SerializedName("questionId") val questionId: String,
		@SerializedName("question") val question: String,
		@SerializedName("answer") val answer: List<Answer>
)

data class Answer(
		@SerializedName("answerId") val answerId: String,
		@SerializedName("answer") val answer: String,
		@SerializedName("isCorrect") val isCorrect: String,
		@SerializedName("point") val point: String
)

