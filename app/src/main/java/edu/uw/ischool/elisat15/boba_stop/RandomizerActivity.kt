package edu.uw.ischool.elisat15.boba_stop

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.content.IntentFilter
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.LocalBroadcastManager

class RandomizerActivity : AppCompatActivity() {

    val TAG: String = "RandomizerActivity"

    private lateinit var broadcastReceiver: BroadcastReceiver
    val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_randomizer)

        val bundle = Bundle()
        bundle.putString("bobaStop", intent.getStringExtra("bobaStop"))

        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = BobaDeciderHome()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

    }

    override fun onStart() {
        super.onStart()
        registerReceiver()
    }

    private fun registerReceiver() {
        Log.v(TAG, "create reciever")

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                val otpCode = intent.getStringExtra("key")
                Log.v(TAG, otpCode)

                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = BobaDeciderResultFragment()
                fragmentTransaction.replace(R.id.fragment_container, fragment)
                fragmentTransaction.commit()
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("intentKey"))
    }

    override fun onStop() {
        super.onStop()
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
        stopService(BobaDataManager.instance.dataManager.serviceIntent)
    }

    override fun onPause() {
        super.onPause()
        stopService(BobaDataManager.instance.dataManager.serviceIntent)
    }

}
