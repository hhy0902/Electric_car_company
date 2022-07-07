package com.example.electric_car_company

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.electric_car_company.databinding.ActivityMapBinding
import com.example.electric_car_company.searchLoad.LoadMap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Boolean.TRUE
import java.lang.Exception
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var latitude = 0.0
    private var longitude = 0.0
    private var infoWindowText = ""
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private var nowLat = 0.0
    private var nowLon = 0.0

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private var cancellationTokenSource : CancellationTokenSource? = null

    private val binding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()

        val url = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving?start=127.1191788,37.485619&goal=127.040980521201,37.5145007522047"

        val lat = intent.getStringExtra("lat")
        val lon = intent.getStringExtra("lon")
        val place = intent.getStringExtra("place")

        Log.d("testt map lat / lon " ,"${lat} / ${lon}")

        latitude = lat!!.toDouble()
        longitude = lon!!.toDouble()
        infoWindowText = place!!

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)

        Log.d("testt location main", "nowLat : ${nowLat}, nowLon : ${nowLon}")


    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        val uiSetting = naverMap.uiSettings

        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(latitude, longitude),12.0)
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

//        나침반은 숨겨져있음 기울기나 방향을 바꿔야지 나타남
//        val cameraPosition = CameraPosition(LatLng(latitude, longitude), 17.0, 50.0,45.0)
//        naverMap.cameraPosition = cameraPosition

        //uiSetting.setCompassEnabled(true)
        uiSetting.isLocationButtonEnabled = true
        uiSetting.isZoomControlEnabled = true

        var marker = Marker()
        marker.position = LatLng(latitude, longitude)
        marker.icon = Marker.DEFAULT_ICON
        marker.width = 100
        marker.height = 100
        marker.map = naverMap

        val infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return infoWindowText
            }
        }
        infoWindow.open(marker)

        val testList = mutableListOf<LatLng>()

        binding.searchLoadButton.setOnClickListener {
            val id = "gxidgfi46e"
            val secret = "bFEPj0IOi8yjUS88rNxTtT74ntkZZWjTzPxMX5PI"
            Log.d("testt location button click", "nowLat : ${nowLat}, nowLon : ${nowLon}")
            Log.d("testt location button click", "Lat : ${latitude}, Lon : ${longitude}")

            val retrofit = Retrofit.Builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retrofitService = retrofit.create(RetrofitService::class.java)
            retrofitService.getLoadMap2(id,secret,"${nowLon},${nowLat}","${longitude},${latitude}").enqueue(object : Callback<LoadMap> {
                override fun onResponse(call: Call<LoadMap>, response: Response<LoadMap>) {
                    if (response.isSuccessful) {
                        val loadMap = response.body()
                        val route = loadMap?.route?.traoptimal
                        Log.d("testt route", "${route}")
                        val summary = route?.get(0)?.summary
                        Log.d("testt summary", "${summary}")
                        val pathLocation = route?.get(0)?.path
                        pathLocation?.forEach {
                            Log.d("testt path", "${it}")
                            testList.add(LatLng(it.get(1), it.get(0)))
                        }
                        val section = route?.get(0)?.section
                        val guide = route?.get(0)?.guide
                        Log.d("testt guide", "${guide}")

                        val path = PathOverlay()
                        path.color = Color.BLUE
                        path.coords = testList
                        path.map = naverMap

                        marker.map = null

                        naverMap.locationTrackingMode = LocationTrackingMode.None

                        val markerStart = Marker()
                        markerStart.position = LatLng(nowLat, nowLon)
                        markerStart.icon = Marker.DEFAULT_ICON
                        markerStart.captionText = "출발"
                        markerStart.setCaptionAligns(Align.Center)
                        markerStart.map = naverMap

                        val markerGoal = Marker()
                        markerGoal.position = LatLng(latitude, longitude)
                        markerGoal.icon = Marker.DEFAULT_ICON
                        markerGoal.icon = MarkerIcons.BLACK
                        markerGoal.iconTintColor = Color.RED
                        markerGoal.setCaptionAligns(Align.Center)
                        markerGoal.captionText = "도착"
                        markerGoal.map = naverMap

                        val infoWindowGoal = InfoWindow()
                        infoWindowGoal.adapter = object : InfoWindow.DefaultTextAdapter(this@MapActivity) {
                            override fun getText(p0: InfoWindow): CharSequence {
                                return infoWindowText
                            }
                        }
                        infoWindowGoal.open(markerGoal)
                    }
                }

                override fun onFailure(call: Call<LoadMap>, t: Throwable) {
                    Log.d("testt onFailure", "${t.message}")
                    Toast.makeText(this@MapActivity, "다시 시도해주세요 ",Toast.LENGTH_SHORT).show()
                }

            })
        }


        Log.d("testt location map", "nowLat : ${nowLat}, nowLon : ${nowLon}")


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("testt", "승낙")

                fetchLocation()

            } else {
                Log.d("testt", "거부")
                naverMap.locationTrackingMode = LocationTrackingMode.None
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        cancellationTokenSource = CancellationTokenSource()
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token
        ).addOnSuccessListener { location ->
            try {
                nowLat = location.latitude
                nowLon = location.longitude
                Log.d("testt location ", "nowLat : ${nowLat}, nowLon : ${nowLon}")

            } catch (e : Exception) {
                e.printStackTrace()
                Toast.makeText(this,"error 발생 다시 시도", Toast.LENGTH_SHORT).show()
            } finally {
                Log.d("testt finish","finish")

            }
        }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 1000
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

    }
}






































