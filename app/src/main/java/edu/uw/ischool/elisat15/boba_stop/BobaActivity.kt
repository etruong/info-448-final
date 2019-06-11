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
import android.support.v4.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_boba_overview.*

class BobaActivity : AppCompatActivity() {

    val TAG: String = "BobaActivity"
    val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boba)

        generateHomeBobaActivity(intent,false)
        Log.v(TAG, "Created boba activity")
    }

    override fun onNewIntent(intent: Intent) {
        Log.v(TAG, "newIntent")
        generateHomeBobaActivity(intent, true)
    }

    private fun generateHomeBobaActivity(thisIntent: Intent, returnStack: Boolean) {

        val chosenBobaStop = thisIntent.getStringExtra("bobaStop")
        BobaDataManager.instance.dataManager.currentBobaStop = chosenBobaStop
        val bundle = Bundle()
        bundle.putString("bobaStop", chosenBobaStop)

        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = BobaOverview()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.boba_activity_id, fragment)
        if (returnStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()

    }

    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause")
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "onStart")
        val serviceIntent = Intent(this, ShakeService::class.java)
        this!!.stopService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume")
    }
}
