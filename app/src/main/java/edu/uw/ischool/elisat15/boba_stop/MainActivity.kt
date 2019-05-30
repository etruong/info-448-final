package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log



class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v(TAG, BobaDataManager.instance.dataManager.bobaData.toString())

//        val myIntent = Intent(this, ShakeService::class.java)
//        startService(myIntent)
    }
}
