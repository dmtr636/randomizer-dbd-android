package com.kodimstudio.myapplication.ui.challenges

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.ui.MainActivity


class SelectChallengesFragment : Fragment() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_select_challenges, container, false)
        val killerButton = root.findViewById<ImageView>(R.id.killerButton)
        val survButton = root.findViewById<ImageView>(R.id.survButton)
        val killerButtonBg = root.findViewById<ImageView>(R.id.killerButtonBg)
        val survButtonBg = root.findViewById<ImageView>(R.id.survButtonBg)

        ViewCompat.setOnApplyWindowInsetsListener(root.findViewById(R.id.top_bar)) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }

        killerButton.setOnClickListener { v: View? ->
            (requireActivity() as MainActivity).navController
                .navigate(R.id.action_select_challenges_to_killer_challenges)
        }
        survButton.setOnClickListener { v: View? ->
            (requireActivity() as MainActivity).navController
                .navigate(R.id.action_select_challenges_to_surv_challenges)
        }

        killerButtonBg.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    killerButton.isPressed = true
                }
                MotionEvent.ACTION_UP -> {
                    killerButton.isPressed = false
                }
            }
            return@setOnTouchListener false
        }
        survButtonBg.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    survButton.isPressed = true
                }
                MotionEvent.ACTION_UP -> {
                    survButton.isPressed = false
                }
            }
            return@setOnTouchListener false
        }
        survButtonBg.setOnClickListener {
            findNavController().navigate(R.id.action_select_challenges_to_surv_challenges)
        }
        killerButtonBg.setOnClickListener {
            findNavController().navigate(R.id.action_select_challenges_to_killer_challenges)
        }

        ViewCompat.setOnApplyWindowInsetsListener(root.findViewById(R.id.buttonsContainer)) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }

        return root
    }
}