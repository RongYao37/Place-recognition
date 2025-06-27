package com.example.placerecognition

import com.google.gson.annotations.SerializedName

data class LocationResult(
    @SerializedName("predicted_latitude")    val predictedLatitude: Double,
    @SerializedName("predicted_longitude")   val predictedLongitude: Double,
    @SerializedName("confidence_score")      val confidenceScore: Double,
    @SerializedName("place_name")            val placeName: String,
    @SerializedName("city")                  val city: String,
    @SerializedName("country")               val country: String,
    @SerializedName("processing_time_ms")    val processingTimeMs: Double
)
