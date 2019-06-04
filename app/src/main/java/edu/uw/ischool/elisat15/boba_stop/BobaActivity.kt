package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class BobaActivity : AppCompatActivity() {
    val TAG: String = "BobaActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boba)

        val intent = intent
        val randomizerIntent = Intent(this, RandomizerActivity::class.java)

        val shopName = findViewById<TextView>(R.id.shopName)
        val shopAddress = findViewById<TextView>(R.id.shopAddress)
        val deciderBtn = findViewById<Button>(R.id.deciderBtn)
        val bobaInfo = BobaDataManager.instance.dataManager
            .returnBobaStop(BobaDataManager.instance.dataManager.currentBobaStop)

        shopName.text = BobaDataManager.instance.dataManager.currentBobaStop
        shopAddress.text = bobaInfo!!.address

        deciderBtn.setOnClickListener {
            randomizerIntent.putExtra("bobaStop", BobaDataManager.instance.dataManager.currentBobaStop)
            startActivity(randomizerIntent)
        }
    }
}
