package com.example.electric_car_company

import com.example.electric_car_company.data.Car
import com.example.electric_car_company.geodata.Location
import com.example.electric_car_company.kakaogeo.LatLon
import com.example.electric_car_company.searchLoad.LoadMap
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitService {

    @GET("openapi/v1/EVcharge.do?apiKey=57a6FT91h23nyi00Iz5GqX41L85I53vNPL8cgG80&returnType=json")
    fun getItem(
        @Query("metroCd") metroCd : String,
        @Query("cityCd") cityCd : String
    ) : Call<Car>

    @GET("map-direction-15/v1/driving?")
    fun getLoadMap(
        @Header("X-NCP-APIGW-API-KEY-ID") id : String,
        @Header("X-NCP-APIGW-API-KEY") secret : String,
        @Query("start") start : String,
        @Query("goal") goal : String
    ) : Call<LoadMap>

    @GET("map-direction/v1/driving?")
    fun getLoadMap2(
        @Header("X-NCP-APIGW-API-KEY-ID") id : String,
        @Header("X-NCP-APIGW-API-KEY") secret : String,
        @Query("start") start : String,
        @Query("goal") goal : String
    ) : Call<LoadMap>

    @GET("map-geocode/v2/geocode?")
    fun getLocation(
        @Header("X-NCP-APIGW-API-KEY-ID") id : String,
        @Header("X-NCP-APIGW-API-KEY") secret : String,
        @Query("query") address : String
    ) : Call<Location>

    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("v2/local/search/address.json")
    fun getLocationKakao(
        @Query("query") address: String
    ) : Call<LatLon>



}














