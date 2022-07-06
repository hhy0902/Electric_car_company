package com.example.electric_car_company

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.electric_car_company.data.Data
import com.example.electric_car_company.kakaogeo.LatLon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarAdapter(
    val stationList : MutableList<Data>,
    val layoutInflater: LayoutInflater,
    val context: Context) : RecyclerView.Adapter<CarAdapter.ViewHolder> () {

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val address : TextView
        val place : TextView
        val carType : TextView

        init {
            address = itemView.findViewById(R.id.address)
            place = itemView.findViewById(R.id.place)
            carType = itemView.findViewById(R.id.carType)

            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "${address.text}", Toast.LENGTH_SHORT).show()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://dapi.kakao.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val retrofitService = retrofit.create(RetrofitService::class.java)
                retrofitService.getLocationKakao(address.text.toString()).enqueue(object : Callback<LatLon> {
                    override fun onResponse(call: Call<LatLon>, response: Response<LatLon>) {
                        if (response.isSuccessful) {
                            val latlon = response.body()
                            val lon = latlon?.documents?.firstOrNull()?.x
                            val lat = latlon?.documents?.firstOrNull()?.y

                            Log.d("testt lat lon", "${lat} / ${lon}")

                            val intent = Intent(itemView.context, MapActivity::class.java)
                            intent.putExtra("lat", lat)
                            intent.putExtra("lon",lon)
                            intent.putExtra("place", place.text)
                            itemView.context.startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<LatLon>, t: Throwable) {

                    }
                })


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarAdapter.ViewHolder {
        val view = layoutInflater.inflate(R.layout.station_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarAdapter.ViewHolder, position: Int) {
        holder.address.text = stationList.get(position).stnAddr
        holder.place.text = stationList.get(position).stnPlace
        holder.carType.text = "지원차종 : "+stationList.get(position).carType
    }

    override fun getItemCount(): Int {
        return stationList.size
    }
}







