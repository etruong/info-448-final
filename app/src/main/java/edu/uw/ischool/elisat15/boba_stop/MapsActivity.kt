package edu.uw.ischool.elisat15.boba_stop

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG: String = "MapsActivity"
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var markerNameDictionary: ArrayList<String> = arrayListOf()

    companion object {
        private const val REQUEST_ACCESS_LOCATION = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // get buttons
        val listButton = findViewById<Button>(R.id.listButton)

        listButton.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            map.uiSettings.isZoomControlsEnabled = true


        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_LOCATION)
        }

        // add all the stores to the map
        val stores = BobaDataManager.instance.dataManager.bobaData
        var index = 0
        for (store in stores) {
            val lat = store.coordinatesLatitude.toDouble()
            val long = store.coordinatesLongitude.toDouble()
            val title = store.name
            val marker = map.addMarker(MarkerOptions()
                .position(LatLng(lat, long))
                .title(title))
            marker.tag = store.id

            // having trouble here... idk how to get it to the boba overview page
            map.setOnInfoWindowClickListener {
                val intent = Intent(this, BobaActivity::class.java)
                BobaDataManager.instance.dataManager.currentBobaStop = it.tag as String
                intent.putExtra("bobaStop", BobaDataManager.instance.dataManager.currentBobaStop)
                startActivity(intent)
            }
        }

        // focuses the map on the user's location
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
            }
        }
    }

}
