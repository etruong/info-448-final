package edu.uw.ischool.elisat15.boba_stop

import android.Manifest
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Checks the connection of the device
        if (!BobaDataManager.instance.dataManager.online) {
            checkConnectivity()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // checks location permission
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    BobaDataManager.instance.dataManager.lastLocation = currentLatLng
                }
            }
        }

        val decideButton = findViewById<Button>(R.id.decide_button)
        val businessInput = findViewById<EditText>(R.id.name_input)

        val intent = Intent(this, MapsActivity::class.java)

        mapButton.setOnClickListener {
            startActivity(intent)
        }

        decideButton.setOnClickListener {
            val myIntent = Intent(this, BobaSearchResultsActivity::class.java)
            myIntent.putExtra("query", businessInput.text.toString())
            startActivity(myIntent)
        }

    }

    fun checkConnectivity() {
        if (Settings.System.getInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0) {

            val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            AlertDialog.Builder(this)
                .setTitle("Airplane Mode?")
                .setMessage("You are currently have no access to the Internet and are in airplane mode. " +
                        "Would you like to turn airplane mode off?")
                .setPositiveButton(android.R.string.yes) { dialog: DialogInterface, which: Int ->
                    startActivity(intent)
                }
                .setNegativeButton(android.R.string.no, null)
                .show()

        } else {

            AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("You currently have no access to the Internet! Unable to download data due to no connection! ")
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.no, null)
                .show()

        }
    }
}
