package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class BobaDeciderHome : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_boba_decider_home, container, false)
        v.findViewById<TextView>(R.id.company_header).text = arguments!!.getString("bobaStop")
        val shakeService = Intent(this.activity, ShakeService::class.java)
        activity!!.startService(shakeService)
        return v
    }

}
