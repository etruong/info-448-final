package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_boba_overview.*

class BobaOverview : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bobaInfo = BobaDataManager.instance.dataManager
            .returnBobaStop(BobaDataManager.instance.dataManager.currentBobaStop)

        shopName.text = BobaDataManager.instance.dataManager.currentBobaStop
        shopAddress.text = bobaInfo!!.address

        val randomizerIntent = Intent(activity, RandomizerActivity::class.java)
        deciderBtn.setOnClickListener {
            randomizerIntent.putExtra("bobaStop", BobaDataManager.instance.dataManager.currentBobaStop)
            startActivity(randomizerIntent)
        }

        menuBtn.setOnClickListener {
            val newFragment = MenuFragment()

            val transaction = fragmentManager!!.beginTransaction().apply {
                replace(R.id.boba_overview_id, newFragment)
                addToBackStack(null)
            }

            transaction.commit()
        }

        shareBtn.setOnClickListener {
            val newFragment = ShareFragment()
            val transaction = fragmentManager!!.beginTransaction().apply {
                replace(R.id.boba_overview_id, newFragment)
                addToBackStack(null)
            }

            transaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boba_overview, container, false)
    }
}
