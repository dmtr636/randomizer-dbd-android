package com.kodimstudio.myapplication.ui.stats.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.adapters.SearchResultListAdapter
import com.kodimstudio.myapplication.databinding.FragmentSearchResultBinding
import com.kodimstudio.myapplication.model.Player
import java.util.*

class SearchResultFragment : Fragment() {

    val onPlayerClickListener = object : SearchResultListAdapter.OnPlayerClickListener{
        @SuppressLint("ApplySharedPref")
        override fun onPlayerClick(player: Player, position: Int) {
            val sp = requireActivity().getSharedPreferences("stats", Context.MODE_PRIVATE)
            sp.edit().putString("playerId", player.steamId.toString()).commit()

            //findNavController().navigate(R.id.action_search_player_result_to_stats)
            findNavController().navigateUp()
            findNavController().navigateUp()
        }
    }

    private lateinit var binding: FragmentSearchResultBinding
    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val root = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(binding.topBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }


        val playersList: MutableList<Player> = mutableListOf()
        val adapter = SearchResultListAdapter(playersList, onPlayerClickListener)

        viewModel.taskCounter.value = 2
        viewModel.playersList.value?.clear()
        viewModel.errorStatus.value = 0

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.errorStatus.observe(viewLifecycleOwner) {status ->
            if (status == -1) {
                Snackbar.make(binding.root, R.string.snackbar_error, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.searchQuery.observe(viewLifecycleOwner) { searchQuery ->
            if (searchQuery != null && playersList.isEmpty()) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                viewModel.playersList.observe(viewLifecycleOwner) { playersList ->

                }
                if (searchQuery.length == 17 && searchQuery.isDigitsOnly()) {
                    viewModel.taskCounter.value = viewModel.taskCounter.value?.plus(1)
                    viewModel.getPlayerSummaries(searchQuery.toLong())
                }

                viewModel.resolveVanityUrl(searchQuery)
                viewModel.searchPlayers(searchQuery)

                viewModel.taskCounter.observe(viewLifecycleOwner) { counter ->
                    if (counter == 0) {
                        binding.progressBar.visibility = ProgressBar.INVISIBLE
                        viewModel.playersList.value?.let {
                            playersList.addAll(it)
                            adapter.notifyItemRangeInserted(0, playersList.size)
                        }
                    }
                }
            }
        }



        return root
    }
}
