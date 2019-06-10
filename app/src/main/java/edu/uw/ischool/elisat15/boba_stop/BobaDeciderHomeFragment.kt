package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_boba_decider_home.*
import org.w3c.dom.Text

class BobaDeciderHome : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val companyHeader = getView()!!.findViewById<TextView>(R.id.company_header)
        companyHeader.text =  BobaDataManager.instance.dataManager
            .returnBobaStop(arguments!!.getString("bobaStop"))!!.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_boba_decider_home, container, false)
        val serviceIntent = Intent(this.activity, ShakeService::class.java)
        this.activity!!.startService(serviceIntent)

        // Inflate the layout for this fragment
        return v
    }

}
