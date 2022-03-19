package com.kodimstudio.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.data.ChallengeDao
import com.kodimstudio.myapplication.model.Challenge
import com.kodimstudio.myapplication.ui.MainActivity

class ChallengesListAdapter(
    private val challenges: List<Challenge>
) : RecyclerView.Adapter<ChallengesListAdapter.ViewHolder>() {

    private val dao: ChallengeDao = MainActivity.db.challengeDao()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.challenges_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.challengeText.text = challenge.text.trim()
        if (challenge.completed) {
            holder.checkboxMark.visibility = View.VISIBLE
        } else {
            holder.checkboxMark.visibility = View.INVISIBLE
        }
        holder.checkboxBg.setOnClickListener {
            challenge.completed = !challenge.completed
            if (challenge.completed) {
                holder.checkboxMark.visibility = View.VISIBLE
            } else {
                holder.checkboxMark.visibility = View.INVISIBLE
            }
            dao.setCompleted(challenge.id, challenge.completed)
        }
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkboxMark: ImageView = view.findViewById(R.id.checkbox_mark)
        val checkboxBg: ImageView = view.findViewById(R.id.checkbox_bg)
        val challengeText: TextView = view.findViewById(R.id.challenge_text)
    }
}