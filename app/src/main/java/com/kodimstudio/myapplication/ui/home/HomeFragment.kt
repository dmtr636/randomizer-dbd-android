package com.kodimstudio.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        with(binding) {
            linkRandomizerText.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_select_randomizer)
            }
            linkChallengesText.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_select_challenges)
            }
            linkGameText.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_skill_check)
            }
            linkStatisticsText.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_stats)
            }
            linkNewsText.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_news)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.linksContainer) { view, windowInsets ->
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

        return root
    }
}