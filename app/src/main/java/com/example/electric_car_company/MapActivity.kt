package com.example.electric_car_company

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.CancellationTokenSource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import java.lang.Boolean.TRUE

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var cancellationTokenSource : CancellationTokenSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        requestPermission()

        val lat = intent.getStringExtra("lat")
        val lon = intent.getStringExtra("lon")

        Log.d("testt map lat / lon " ,"${lat} / ${lon}")

        latitude = lat!!.toDouble()
        longitude = lon!!.toDouble()

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        val uiSetting = naverMap.uiSettings

        val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(latitude, longitude),17.0)
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

//        나침반은 숨겨져있음 기울기나 방향을 바꿔야지 나타남
//        val cameraPosition = CameraPosition(LatLng(latitude, longitude), 17.0, 50.0,45.0)
//        naverMap.cameraPosition = cameraPosition

        //uiSetting.setCompassEnabled(true)
        uiSetting.isLocationButtonEnabled = true
        uiSetting.isZoomControlEnabled = true

        val marker = Marker()
        marker.position = LatLng(latitude, longitude)
        marker.icon = Marker.DEFAULT_ICON
        marker.map = naverMap

        naverMap.mapType = NaverMap.MapType.Basic
        naverMap.locationTrackingMode = LocationTrackingMode.Follow




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


            } else {
                Log.d("testt", "거부")
                naverMap.locationTrackingMode = LocationTrackingMode.None
                finish()
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

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
    }

    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 1000
    }
}






































