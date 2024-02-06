package com.example.animalsapp.domain.model

import com.google.gson.annotations.SerializedName

data class CatModel(
    @SerializedName("height")
    val height: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("width")
    val width: Int?
)