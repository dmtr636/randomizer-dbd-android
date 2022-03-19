package com.kodimstudio.myapplication.ui.stats.tabs

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.ui.FavouritePlayersActivity
import com.kodimstudio.myapplication.model.Stat
import com.kodimstudio.myapplication.ui.stats.StatsFragment
import com.kodimstudio.myapplication.adapters.StatsListAdapter

class GeneralStatsFragment : Fragment() {
    private lateinit var rankKiller: ImageView
    private lateinit var rankSurv: ImageView
    private lateinit var killerGradeText: TextView
    private lateinit var survGradeText: TextView
    private lateinit var rankTextView: TextView
    private lateinit var generalStatsListView: ListView
    private lateinit var favouritePlayers: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_general_stats, container, false)
        rankKiller = root.findViewById(R.id.rank_killer)
        rankSurv = root.findViewById(R.id.rank_surv)
        killerGradeText = root.findViewById(R.id.killer_grade_text)
        survGradeText = root.findViewById(R.id.surv_grade_text)
        generalStatsListView = root.findViewById(R.id.generalStatsListView)
        rankTextView = root.findViewById(R.id.rankTextView)
        rankTextView.visibility = View.INVISIBLE
        rankTextView.visibility = View.VISIBLE

        favouritePlayers = root.findViewById(R.id.favouritePlayers)
        ViewCompat.setOnApplyWindowInsetsListener(favouritePlayers) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }

        favouritePlayers.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.isPressed = true
                }
                MotionEvent.ACTION_UP -> {
                    v.isPressed = false
                }
            }
            return@setOnTouchListener false
        }

        favouritePlayers.setOnClickListener {
            startActivity(Intent(requireContext(), FavouritePlayersActivity::class.java))
        }

        var rankKillerValue = 0
        if (StatsFragment.playerStatsMap.containsKey("DBD_KillerSkulls")) {
            rankKillerValue = StatsFragment.playerStatsMap["DBD_KillerSkulls"]!!
        }
        var rankSurvValue = 0
        if (StatsFragment.playerStatsMap.containsKey("DBD_CamperSkulls")) {
            rankSurvValue = StatsFragment.playerStatsMap["DBD_CamperSkulls"]!!
        }
        val frameKillerNumber: Int = if (rankKillerValue <= 6) {
            20 - rankKillerValue / 3
        } else if (rankKillerValue <= 30) {
            18 - (rankKillerValue - 6) / 4
        } else if (rankKillerValue <= 85) {
            12 - (rankKillerValue - 30) / 5
        } else {
            1
        }
        var killerGradeIconName = "icongrade_killerash"
        when (frameKillerNumber) {
            1 -> {
                killerGradeIconName = "icongrade_killeriridescent_1"
                killerGradeText.text = integerToRoman(1)
                killerGradeText.setTextColor(Color.parseColor("#ff0000"))
            }
            2, 3, 4 -> {
                killerGradeIconName = "icongrade_killeriridescent"
                killerGradeText.text = integerToRoman((frameKillerNumber - 1) % 4 + 1)
                killerGradeText.setTextColor(Color.parseColor("#ff0000"))
            }
            5, 6, 7, 8 -> {
                killerGradeIconName = "icongrade_killergold"
                killerGradeText.text = integerToRoman((frameKillerNumber - 1) % 4 + 1)
                killerGradeText.setTextColor(Color.parseColor("#ffa800"))
            }
            9, 10, 11, 12 -> {
                killerGradeIconName = "icongrade_killersilver"
                killerGradeText.text = integerToRoman((frameKillerNumber - 1) % 4 + 1)
                killerGradeText.setTextColor(Color.parseColor("#b5afb0"))
            }
            13, 14, 15, 16 -> {
                killerGradeIconName = "icongrade_killerbronze"
                killerGradeText.text = integerToRoman((frameKillerNumber - 1) % 4 + 1)
                killerGradeText.setTextColor(Color.parseColor("#c2593a"))
            }
            17, 18, 19, 20 -> {
                killerGradeIconName = "icongrade_killerash"
                killerGradeText.text = integerToRoman((frameKillerNumber - 1) % 4 + 1)
                killerGradeText.setTextColor(Color.parseColor("#808080"))
            }
        }
        rankKiller.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                resources.getIdentifier(
                    killerGradeIconName,
                    "drawable",
                    requireContext().packageName
                ),
                null
            )
        )
        var frameSurvNumber = 0
        frameSurvNumber = if (rankSurvValue <= 6) {
            20 - rankSurvValue / 3
        } else if (rankSurvValue <= 30) {
            18 - (rankSurvValue - 6) / 4
        } else if (rankSurvValue <= 85) {
            12 - (rankSurvValue - 30) / 5
        } else {
            1
        }
        var survGradeIconName = "icongrade_survivorash"
        when (frameSurvNumber) {
            1 -> {
                survGradeIconName = "icongrade_survivoriridescent_1"
                survGradeText.text = integerToRoman(1)
                survGradeText.setTextColor(Color.parseColor("#ff0000"))
            }
            2, 3, 4 -> {
                survGradeIconName = "icongrade_survivoriridescent"
                survGradeText.text = integerToRoman((frameSurvNumber - 1) % 4 + 1)
                survGradeText.setTextColor(Color.parseColor("#ff0000"))
            }
            5, 6, 7, 8 -> {
                survGradeIconName = "icongrade_survivorgold"
                survGradeText.text = integerToRoman((frameSurvNumber - 1) % 4 + 1)
                survGradeText.setTextColor(Color.parseColor("#ffa800"))
            }
            9, 10, 11, 12 -> {
                survGradeIconName = "icongrade_survivorsilver"
                survGradeText.text = integerToRoman((frameSurvNumber - 1) % 4 + 1)
                survGradeText.setTextColor(Color.parseColor("#b5afb0"))
            }
            13, 14, 15, 16 -> {
                survGradeIconName = "icongrade_survivorbronze"
                survGradeText.text = integerToRoman((frameSurvNumber - 1) % 4 + 1)
                survGradeText.setTextColor(Color.parseColor("#c2593a"))
            }
            17, 18, 19, 20 -> {
                survGradeIconName = "icongrade_survivorash"
                survGradeText.text = integerToRoman((frameSurvNumber - 1) % 4 + 1)
                survGradeText.setTextColor(Color.parseColor("#808080"))
            }
        }
        rankSurv.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                resources.getIdentifier(
                    survGradeIconName,
                    "drawable",
                    requireContext().packageName
                ),
                null
            )
        )
        val statsList: MutableList<Stat> = ArrayList()
        if (StatsFragment.playerStatsMap.containsKey("DBD_BloodwebPoints")) {
            statsList.add(
                Stat(
                    R.string.DBD_BloodwebPoints,
                    StatsFragment.playerStatsMap["DBD_BloodwebPoints"].toString()
                )
            )
        }
        if (StatsFragment.playerStatsMap.containsKey("DBD_MaxBloodwebPointsOneCategory")) {
            statsList.add(
                Stat(
                    R.string.DBD_MaxBloodwebPointsOneCategory,
                    StatsFragment.playerStatsMap["DBD_MaxBloodwebPointsOneCategory"].toString()
                )
            )
        }
        if (StatsFragment.playerStatsMap.containsKey("DBD_BloodwebMaxPrestigeLevel")) {
            statsList.add(
                Stat(
                    R.string.DBD_BloodwebMaxPrestigeLevel,
                    StatsFragment.playerStatsMap["DBD_BloodwebMaxPrestigeLevel"].toString()
                )
            )
        }
        if (StatsFragment.playerStatsMap.containsKey("DBD_UnlockRanking")) {
            statsList.add(
                Stat(
                    R.string.DBD_UnlockRanking,
                    StatsFragment.playerStatsMap["DBD_UnlockRanking"].toString()
                )
            )
        }
        if (StatsFragment.playerStatsMap.containsKey("DBD_BurnOffering_UltraRare")) {
            statsList.add(
                Stat(
                    R.string.DBD_BurnOffering_UltraRare,
                    StatsFragment.playerStatsMap["DBD_BurnOffering_UltraRare"].toString()
                )
            )
        }
        if (StatsFragment.playerStatsMap.containsKey("DBD_TotalAchievements")) {
            statsList.add(
                Stat(
                    R.string.DBD_TotalAchievements,
                    StatsFragment.playerStatsMap["DBD_TotalAchievements"].toString() + "/182"
                )
            )
        }
        val adapter = StatsListAdapter(
            requireContext(),
            R.layout.stats_list_item,
            statsList
        )
        generalStatsListView.adapter = adapter
        return root
    }

    companion object {
        fun integerToRoman(num: Int): String {
            var num = num
            println("Integer: $num")
            val values = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
            val romanLiterals =
                arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
            val roman = StringBuilder()
            for (i in values.indices) {
                while (num >= values[i]) {
                    num -= values[i]
                    roman.append(romanLiterals[i])
                }
            }
            return roman.toString()
        }
    }
}