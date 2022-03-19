package com.kodimstudio.myapplication.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.model.Perk

class PerksListAdapter(
    private val perks: MutableList<Perk>
) : RecyclerView.Adapter<PerksListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.perk_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val perk = perks[position]
        holder.perkIcon.setImageResource(perk.imageResourceId)
        if (perk.excluded) {
            holder.perkIconBg.setImageResource(R.drawable.perk_icon_bg_gray)
            holder.perkIcon.alpha = 0.5f
        } else {
            holder.perkIconBg.setImageResource(R.drawable.perk_icon_bg)
            holder.perkIcon.alpha = 1.0f
        }
        holder.itemView.setOnClickListener {
            perk.excluded = !perk.excluded
            if (perk.excluded) {
                holder.perkIconBg.setImageResource(R.drawable.perk_icon_bg_gray)
                holder.perkIcon.alpha = 0.5f
            } else {
                holder.perkIconBg.setImageResource(R.drawable.perk_icon_bg)
                holder.perkIcon.alpha = 1.0f
            }
        }
    }

    override fun getItemCount(): Int {
        return perks.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val perkIcon: ImageView = view.findViewById(R.id.perk_icon)
        val perkIconBg: ImageView = view.findViewById(R.id.perk_bg_icon)
    }
}