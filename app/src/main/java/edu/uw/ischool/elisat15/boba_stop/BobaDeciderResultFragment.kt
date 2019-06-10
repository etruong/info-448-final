package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import kotlin.math.roundToInt
import kotlin.random.Random


class BobaDeciderResultFragment : Fragment() {

    val list: Array<String> = arrayOf("Taro Milk Tea", "Honey Dew Milk Tea", "Thai Milk Tea")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_boba_decider_result, container, false)
        v.findViewById<TextView>(R.id.company_header2).text = BobaDataManager.instance.dataManager.returnCurrentBobaStop()!!.name

        val results = BobaDataManager.instance.dataManager.returnRandomBoba(BobaDataManager.instance.dataManager.currentBobaStop)
        v.findViewById<TextView>(R.id.decider_result).text = results!!.name

        v.findViewById<TextView>(R.id.boba_category).text = results.category

        return v
    }

    override fun onStart() {
        super.onStart()
        this.activity!!.startService(BobaDataManager.instance.dataManager.serviceIntent)
    }
}
