package edu.uw.ischool.elisat15.boba_stop

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.support.v4.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_boba_overview.*

class BobaActivity : AppCompatActivity() {
    val TAG: String = "BobaActivity"
    val fragmentManager = supportFragmentManager
    private lateinit var broadcastReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boba)
        
        //BobaDataManager.instance.dataManager.fetchBobaMenuData(this)

        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = BobaOverview()
        fragmentTransaction.replace(R.id.boba_activity_id, fragment)
        fragmentTransaction.commit()

    }

//    override fun onStart() {
//        super.onStart()
//        registerReceiver()
//    }
//
//    private fun registerReceiver() {
//        Log.v(TAG, "create reciever")
//
//        val activityContext = this
//
//        broadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//
//                val otpCode = intent.getStringExtra("key")
//                Log.v(TAG, otpCode)
//
//                val fragmentTransaction = fragmentManager.beginTransaction()
//                val fragment = MenuFragment()
//                fragmentTransaction.replace(R.id.boba_activity_id, fragment)
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.commit()
//            }
//        }
//        registerReceiver(broadcastReceiver, IntentFilter("intentKey"))
//    }
//
//    override fun onStop() {
//        super.onStop()
//        if (broadcastReceiver != null) {
//            unregisterReceiver(broadcastReceiver)
//        }
//    }
}
