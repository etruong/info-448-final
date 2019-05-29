package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class RandomizerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_randomizer)

        val myIntent = Intent(this, ShakeService::class.java)
        startService(myIntent)
    }
}
