package com.example.electric_car_company

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.electric_car_company.data.Car
import com.example.electric_car_company.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var metroCd = "" // 시도
    var cityCd = "" // 군구

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val url = "https://bigdata.kepco.co.kr/openapi/v1/EVcharge.do?metroCd=11&cityCd=26&apiKey=57a6FT91h23nyi00Iz5GqX41L85I53vNPL8cgG80&returnType=json"
        val url2 = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=자곡로3길 21"

        binding.search.setOnClickListener {
            metroCd = binding.sido.text.toString()
            cityCd = binding.gugon.text.toString()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://bigdata.kepco.co.kr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retrofitService = retrofit.create(RetrofitService::class.java)
            retrofitService.getItem(metroCd, cityCd).enqueue(object : Callback<Car> {
                override fun onResponse(call: Call<Car>, response: Response<Car>) {
                    if (response.isSuccessful) {
                        val station = response.body()
                        val stationList = station?.data

                        binding.recyclerView.adapter = CarAdapter(stationList!!, LayoutInflater.from(this@MainActivity), this@MainActivity)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                        station?.data?.forEach {
                            Log.d("testt station","${it}")
                        }
                    }
                }

                override fun onFailure(call: Call<Car>, t: Throwable) {
                    Log.d("testt onFailure message", "${t.message}")
                }

            })
        }
    }
}





































