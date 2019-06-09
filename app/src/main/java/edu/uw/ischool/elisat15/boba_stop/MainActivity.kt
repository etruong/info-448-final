package edu.uw.ischool.elisat15.boba_stop

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Checks the connection of the device
        if (!BobaDataManager.instance.dataManager.online) {
            checkConnectivity()
        }

        val decideButton = findViewById<Button>(R.id.decide_button)
        val businessInput = findViewById<EditText>(R.id.name_input)

        val shakeService = Intent(this, ShakeService::class.java)
        val myIntent = Intent(this, BobaActivity::class.java)

        decideButton.setOnClickListener {
            myIntent.putExtra("bobaStop", businessInput.text.toString())
            BobaDataManager.instance.dataManager.currentBobaStop = businessInput.text.toString()
            startService(shakeService)
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
