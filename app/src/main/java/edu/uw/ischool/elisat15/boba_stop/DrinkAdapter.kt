package edu.uw.ischool.elisat15.boba_stop

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.drink_list_item.view.*

/**
 * This class will generate the recycled views and load data when they come into screen using view holder pattern
 */
class DrinkAdapter(var listOfNames: List<String>): RecyclerView.Adapter<DrinkAdapter.PersonViewHolder>() {

    var onPersonClickedListener: ((position: Int, name: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewHolderType: Int): PersonViewHolder {
        // Creates ViewHolder to hold reference of the views
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.drink_list_item, parent, false)
        return PersonViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        // Size of items to load
        return listOfNames.size
    }

    override fun onBindViewHolder(viewHolder: PersonViewHolder, position: Int) {
        // Sets data on view
        viewHolder.bindView(listOfNames[position], position)
    }

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(personName: String, position: Int) {
            itemView.drinkName.text = personName

            itemView.setOnClickListener {
                onPersonClickedListener?.invoke(position, personName)
            }
        }
    }
}

