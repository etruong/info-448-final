package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
