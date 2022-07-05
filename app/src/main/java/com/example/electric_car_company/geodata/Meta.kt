package com.example.electric_car_company.geodata

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)