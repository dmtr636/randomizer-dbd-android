package com.kodimstudio.myapplication.ui.challenges

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.adapters.ChallengesListAdapter
import com.kodimstudio.myapplication.databinding.FragmentChallengesListKillerBinding
import com.kodimstudio.myapplication.ui.MainActivity

class ChallengesListKillerFragment : Fragment() {
    private lateinit var binding: FragmentChallengesListKillerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengesListKillerBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.resetButtonBg) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.topBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }


        val dao = MainActivity.db.challengeDao()
        val challenges = dao.getAll(type = "killer")
        val adapter = ChallengesListAdapter(challenges)
        binding.challengesList.adapter = adapter
        binding.challengesList.layoutManager = LinearLayoutManager(requireContext())

        binding.resetButtonText.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_reset_challenges)
            val resetButton: TextView = dialog.findViewById(R.id.resetButton)
            val cancelButton: TextView = dialog.findViewById(R.id.cancelButton)

            resetButton.setOnClickListener {
                challenges.forEach {
                    it.completed = false
                    dao.setCompleted(it.id, it.completed)
                }
                dialog.cancel()
                adapter.notifyItemRangeChanged(0, challenges.size)
            }

            cancelButton.setOnClickListener {
                dialog.cancel()
            }

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        return binding.root
    }
}