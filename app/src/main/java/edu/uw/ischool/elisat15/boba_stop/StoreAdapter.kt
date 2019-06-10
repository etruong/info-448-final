package edu.uw.ischool.elisat15.boba_stop

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.drink_list_item.view.*

/**
 * This class will generate the recycled views and load data when they come into screen using view holder pattern
 */
class StoreAdapter(private val mContext: Context, var listOfNames: List<String>): RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewHolderType: Int): StoreViewHolder {
        // Creates ViewHolder to hold reference of the views
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.drink_list_item, parent, false)
        return StoreViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        // Size of items to load
        return listOfNames.size
    }

    override fun onBindViewHolder(viewHolder: StoreViewHolder, position: Int) {
        // Sets data on view
        viewHolder.bindView(listOfNames[position])
    }

    inner class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(storeName: String) {
            itemView.drinkName.text = storeName

            itemView.setOnClickListener {
                val intent = Intent(mContext, BobaActivity::class.java)
                BobaDataManager.instance.dataManager.currentBobaStop = it.tag as String
                intent.putExtra("bobaStop", BobaDataManager.instance.dataManager.currentBobaStop)
                mContext.startActivity(intent)
            }
        }
    }
}