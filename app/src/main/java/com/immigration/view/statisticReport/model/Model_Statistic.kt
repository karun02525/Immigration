package com.immigration.view.statisticReport.model

import com.google.gson.annotations.SerializedName


data class Model_Statistic(
        @SerializedName("message") val message: String,
        @SerializedName("result") val result: List<Result>
)

data class Result(
        @SerializedName("immigrationId") val immigrationId: String,
        @SerializedName("name") val name: String,
        @SerializedName("points") val points: String,
        @SerializedName("createdAt") val createdAt: String,
//PDF Data
        @SerializedName("pdfId") val pdfId: String,
        @SerializedName("pdf") val pdf: String,
        @SerializedName("details") val details: String,
        @SerializedName("cost") val cost: String,
        var ischeckDwonload:Boolean=false
)