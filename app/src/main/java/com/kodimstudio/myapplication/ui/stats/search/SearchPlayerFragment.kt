package com.kodimstudio.myapplication.ui.stats.search

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.databinding.FragmentSearchPlayerBinding


class SearchPlayerFragment : Fragment() {
    private lateinit var binding: FragmentSearchPlayerBinding
    private val viewModel: SearchViewModel by activityViewModels()

    companion object {
        var visited = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchPlayerBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.topBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.inputFieldLayout) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            binding.inputFieldLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = imeHeight
            }
            insets
        }

        visited = true

        binding.searchButton.setOnClickListener {
            if (binding.inputField.text.isEmpty()) {
                Snackbar.make(binding.root, getString(R.string.fill_search_field), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.searchQuery.value = binding.inputField.text.toString()
            findNavController().navigate(R.id.action_search_player_to_search_player_result)
        }

        binding.helpButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_search_player_help)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        return binding.root
    }
}