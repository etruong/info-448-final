package edu.uw.ischool.elisat15.boba_stop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class BobaActivity : AppCompatActivity() {
    val TAG: String = "BobaActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boba)

        val intent = intent

        val shopName = findViewById<TextView>(R.id.shopName)
        shopName.text = intent.getStringExtra("shopName")
    }
}
