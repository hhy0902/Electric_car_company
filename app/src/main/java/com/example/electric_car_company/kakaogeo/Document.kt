package com.example.electric_car_company.kakaogeo

import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("address")
    val address: Address?,
    @SerializedName("address_name")
    val addressName: String?,
    @SerializedName("address_type")
    val addressType: String?,
    @SerializedName("road_address")
    val roadAddress: RoadAddress?,
    @SerializedName("x")
    val x: String?,
    @SerializedName("y")
    val y: String?
)