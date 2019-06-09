package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager


class BobaActivity : AppCompatActivity() {

    val TAG: String = "BobaActivity"

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boba)

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter("some filter name"));

        val intent = intent
        val extras = getIntent().getExtras()
        val randomizerIntent = Intent(this, RandomizerActivity::class.java)

        val shopName = findViewById<TextView>(R.id.shopName)
        val shopAddress = findViewById<TextView>(R.id.shopAddress)
        val deciderBtn = findViewById<Button>(R.id.deciderBtn)
        val bobaInfo = BobaDataManager.instance.dataManager
            .returnBobaStop(extras.getString("bobaStop"))

        shopName.text = extras.getString("bobaStop")
        shopAddress.text = bobaInfo!!.address

        deciderBtn.setOnClickListener {
            randomizerIntent.putExtra("bobaStop", BobaDataManager.instance.dataManager.currentBobaStop)
            startActivity(randomizerIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        val shopName = findViewById<TextView>(R.id.shopName)
        shopName.text = BobaDataManager.instance.dataManager.currentBobaStop
    }
}
