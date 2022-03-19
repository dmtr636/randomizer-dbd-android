package com.kodimstudio.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.model.Character

class CharactersListAdapter(
    private val characters: MutableList<Character>
) : RecyclerView.Adapter<CharactersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.characters_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.characterIcon.setImageResource(character.imageResourceId)
        if (character.selected) {
            holder.characterIconBorder.setImageResource(R.drawable.char_icon_border_selected)
        } else {
            holder.characterIconBorder.setImageResource(R.drawable.char_icon_border)
        }
        holder.itemView.setOnClickListener {
            character.selected = !character.selected
            if (character.selected) {
                holder.characterIconBorder.setImageResource(R.drawable.char_icon_border_selected)
            } else {
                holder.characterIconBorder.setImageResource(R.drawable.char_icon_border)
            }
        }
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val characterIcon: ImageView = view.findViewById(R.id.char_icon)
        val characterIconBorder: ImageView = view.findViewById(R.id.char_icon_border)
    }
}