package com.kodimstudio.myapplication.ui.challenges

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.databinding.FragmentChallengesSurvBinding
import com.kodimstudio.myapplication.ui.MainActivity


class ChallengesSurvFragment : Fragment() {
    private lateinit var binding: FragmentChallengesSurvBinding


    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengesSurvBinding.inflate(inflater, container, false)

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


        val dao = MainActivity.db.challengeDao()
        val killerChallenges = dao.getAll(type = "killer")
        var uncompletedKillerChallenges = dao.getUncompleted(type = "killer")

        val survivorChallenges = dao.getAll(type = "survivor")
        var uncompletedSurvivorChallenges = dao.getUncompleted(type = "survivor")

        binding.killerCount.text = "${killerChallenges.size - uncompletedKillerChallenges.size} / ${killerChallenges.size}"
        binding.survCount.text = "${survivorChallenges.size - uncompletedSurvivorChallenges.size} / ${survivorChallenges.size}"

        val sp = requireActivity().getSharedPreferences("challenges", Context.MODE_PRIVATE)
        val currentChallengeId = sp.getInt("currentSurvivorChallengeId", 1000)
        var currentChallenge = survivorChallenges.find {
            it.id == currentChallengeId
        }!!

        binding.challengeName.text = currentChallenge.name
        binding.challengeText.text = currentChallenge.text
        binding.checkboxMark.alpha = if (currentChallenge.completed) 1.0f else 0.3f

        binding.randomizeButton.setOnClickListener {
            uncompletedKillerChallenges = dao.getUncompleted(type = "survivor")
            val otherUncompletedChallenges = uncompletedKillerChallenges.filter {
                it.id != currentChallenge.id
            }
            if (otherUncompletedChallenges.isNotEmpty()) {
                with(otherUncompletedChallenges.random()) {
                    currentChallenge = this
                    binding.challengeName.text = this.name
                    binding.challengeText.text = this.text
                    binding.checkboxMark.alpha = if (this.completed) 1.0f else 0.3f
                    sp.edit().putInt("currentSurvivorChallengeId", this.id).apply()
                }
            } else {
                Snackbar.make(binding.root, R.string.challenges_over, Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.checkboxBg.setOnClickListener {
            with(binding.checkboxMark) {
                if (currentChallenge.completed) {
                    currentChallenge.completed = false
                    binding.checkboxMark.alpha = 0.3f
                    dao.setCompleted(currentChallenge.id, false)
                } else {
                    currentChallenge.completed = true
                    binding.checkboxMark.alpha = 1.0f
                    dao.setCompleted(currentChallenge.id, true)
                }
                uncompletedSurvivorChallenges = dao.getUncompleted(type = "survivor")
                binding.survCount.text = "${survivorChallenges.size - uncompletedSurvivorChallenges.size} / ${survivorChallenges.size}"
            }
        }

        binding.killerChallengesButton.setOnClickListener {
            findNavController().navigate(R.id.action_surv_challenges_to_list_killer_challenges)
        }
        binding.survChallengesButton.setOnClickListener {
            findNavController().navigate(R.id.action_surv_challenges_to_list_surv_challenges)
        }

        binding.randomizeButton.setOnTouchListener { v, event ->
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
            return@setOnTouchListener false
        }

        return binding.root
    }
}