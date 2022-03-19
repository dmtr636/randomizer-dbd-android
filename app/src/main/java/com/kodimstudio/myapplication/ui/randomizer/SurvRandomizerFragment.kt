package com.kodimstudio.myapplication.ui.randomizer

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.adapters.CharactersListAdapter
import com.kodimstudio.myapplication.adapters.PerksListAdapter
import com.kodimstudio.myapplication.data.Data
import com.kodimstudio.myapplication.databinding.FragmentSurvRandomizerBinding
import com.kodimstudio.myapplication.model.Character


class SurvRandomizerFragment : Fragment() {
    private lateinit var binding: FragmentSurvRandomizerBinding
    private val perks = Data.survivorPerks
    private val characters = Data.survivorCharacters
    private lateinit var perksAdapter: PerksListAdapter
    private lateinit var charactersAdapter: CharactersListAdapter

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurvRandomizerBinding.inflate(inflater, container, false)
        val root = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(binding.randomizeButton) { view, windowInsets ->
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

        val sp = requireActivity().getSharedPreferences("randomizer", Context.MODE_PRIVATE)

        characters.forEach {
            it.selected = false
        }
        perks.forEach {
            it.excluded = false
        }

        if (sp.getBoolean("saveChoice", true)) {
            val selectedCharactersIndexes = sp.getStringSet("selectedSurvivorsCharactersIndexes", null)
            val excludedPerksIndexes = sp.getStringSet("excludedSurvivorsPerksIndexes", null)

            selectedCharactersIndexes?.forEach {
                characters[it.toInt()].selected = true
            }
            excludedPerksIndexes?.forEach {
                perks[it.toInt()].excluded = true
            }
        }

        binding.settingsButton.setOnClickListener { view ->
            val popup = PopupMenu(requireContext(), view)
            popup.menuInflater.inflate(R.menu.randomizer_menu, popup.menu)

            if (sp.getBoolean("saveChoice", true)) {
                popup.menu.findItem(R.id.action_save_choice).isChecked = true
            }

            popup.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.action_save_choice -> {
                        if (!menuItem.isChecked) {
                            menuItem.isChecked = true
                            sp.edit().putBoolean("saveChoice", true).apply()
                        } else {
                            menuItem.isChecked = false
                            sp.edit().putBoolean("saveChoice", false).apply()
                        }
                    }
                    R.id.action_clear_choice -> {
                        characters.forEach {
                            it.selected = false
                        }
                        perks.forEach {
                            it.excluded = false
                        }
                        charactersAdapter.notifyItemRangeChanged(0, characters.size)
                        perksAdapter.notifyItemRangeChanged(0, perks.size)
                    }
                }
                return@setOnMenuItemClickListener true
            }

            val menuHelper = MenuPopupHelper(
                requireContext(),
                (popup.menu as MenuBuilder?)!!, view
            )
            menuHelper.setForceShowIcon(true)
            menuHelper.show()
        }

        binding.helpButton.setOnClickListener {
            with(Dialog(requireContext())) {
                setContentView(R.layout.dialog_surv_randomizer_help)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()
            }
        }

        perksAdapter = PerksListAdapter(perks)
        binding.perksList.adapter = perksAdapter
        binding.perksList.layoutManager = GridLayoutManager(context, 3, RecyclerView.HORIZONTAL, false)

        charactersAdapter = CharactersListAdapter(characters)
        binding.charactersList.adapter = charactersAdapter
        binding.charactersList.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_surv_randomizer_result)
        val perk1 = dialog.findViewById<ImageView>(R.id.perk_icon1)
        val perk2 = dialog.findViewById<ImageView>(R.id.perk_icon2)
        val perk3 = dialog.findViewById<ImageView>(R.id.perk_icon3)
        val perk4 = dialog.findViewById<ImageView>(R.id.perk_icon4)
        val namePerk1 = dialog.findViewById<TextView>(R.id.perk_text1)
        val namePerk2 = dialog.findViewById<TextView>(R.id.perk_text2)
        val namePerk3 = dialog.findViewById<TextView>(R.id.perk_text3)
        val namePerk4 = dialog.findViewById<TextView>(R.id.perk_text4)
        val iconChar = dialog.findViewById<ImageView>(R.id.char_icon)
        val iconAddon1 = dialog.findViewById<ImageView>(R.id.addon_icon1)
        val iconAddon2 = dialog.findViewById<ImageView>(R.id.addon_icon2)
        val iconItem = dialog.findViewById<ImageView>(R.id.item_icon)
        val randomizeButton = root.findViewById<ImageView>(R.id.randomizeButton)

        randomizeButton.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.2f).apply {
                        duration = 200
                        start()
                    }
                    ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.2f).apply {
                        duration = 200
                        start()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    v.performClick()
                    ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1f).apply {
                        duration = 200
                        start()
                    }
                    ObjectAnimator.ofFloat(v, "scaleY", 1.2f, 1f).apply {
                        duration = 200
                        start()
                    }
                }
            }
            true
        }

        randomizeButton.setOnClickListener {
            val selectedCharacters = characters.filter { it.selected }

            val randomizedCharacter: Character = if (selectedCharacters.isEmpty()) {
                characters.random()
            } else {
                selectedCharacters.random()
            }
            iconChar.setImageResource(randomizedCharacter.imageResourceId)

            val selectedPerks = perks.filter { !it.excluded }
            if (selectedPerks.size < 4) {
                Toast.makeText(context, R.string.selected_few_perks, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val shuffledPerks = selectedPerks.shuffled()

            perk1.setImageResource(shuffledPerks[0].imageResourceId)
            perk2.setImageResource(shuffledPerks[1].imageResourceId)
            perk3.setImageResource(shuffledPerks[2].imageResourceId)
            perk4.setImageResource(shuffledPerks[3].imageResourceId)
            namePerk1.text = shuffledPerks[0].name
            namePerk2.text = shuffledPerks[1].name
            namePerk3.text = shuffledPerks[2].name
            namePerk4.text = shuffledPerks[3].name

            val items = Data.items
            val randomItem = items.random()
            val randomItemVariant = randomItem.itemImageResIds.random()
            val shuffledItemAddons = randomItem.itemImageAddonsResIds.shuffled()

            iconItem.setImageResource(randomItemVariant)
            iconAddon1.setImageResource(shuffledItemAddons[0])
            iconAddon2.setImageResource(shuffledItemAddons[1])

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()

            val selectedCharactersIndexes = characters.mapIndexed { index, character ->
                if (character.selected)
                    index.toString()
                else
                    "-1"
            }.filter { it != "-1" }

            val excludedPerksIndexes = perks.mapIndexed { index, perk ->
                if (perk.excluded)
                    index.toString()
                else
                    "-1"
            }.filter { it != "-1" }

            if (sp.getBoolean("saveChoice", true)) {
                sp.edit()
                    .putStringSet("selectedSurvivorsCharactersIndexes", selectedCharactersIndexes.toMutableSet())
                    .putStringSet("excludedSurvivorsPerksIndexes", excludedPerksIndexes.toMutableSet())
                    .apply()
            }
        }

        return root
    }
}