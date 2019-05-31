package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button


class RandomizerActivity : AppCompatActivity() {

    val TAG: String = "RandomizerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_randomizer)

        val intent = intent
        Log.v(TAG, intent.getStringExtra("bobaStop"))

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = BobaDeciderHome()
        fragmentTransaction.add(R.id.randomizer_activity, fragment)
        fragmentTransaction.commit()
    }
}
