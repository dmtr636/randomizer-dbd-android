package com.kodimstudio.myapplication.ui.stats.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.model.Stat
import com.kodimstudio.myapplication.ui.stats.StatsFragment
import com.kodimstudio.myapplication.adapters.StatsListAdapter
import java.util.ArrayList

class SurvStatsFragment : Fragment() {
    private lateinit var survStatsListView: ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_surv_stats, container, false)
        survStatsListView = root.findViewById(R.id.survStatsListView)
        val statsList: MutableList<Stat> = ArrayList()
        val statsKeysList = arrayOf(
            "DBD_CamperMaxScoreByCategory",
            "DBD_CamperFullLoadout",
            "DBD_CamperKeepUltraRare",
            "DBD_Chapter17_Camper_Stat1",
            "DBD_HealPct_float",
            "DBD_Chapter11_Camper_Stat1_float",
            "DBD_Chapter17_Camper_Stat2_float",
            "DBD_Chapter15_Camper_Stat1",
            "DBD_UnhookOrHeal",
            "DBD_UnhookOrHeal_PostExit",
            "DBD_Chapter18_Camper_Stat1",
            "DBD_Chapter9_Camper_Stat1",
            "DBD_SkillCheckSuccess",
            "DBD_Chapter10_Camper_Stat1",
            "DBD_DLC3_Camper_Stat1",
            "DBD_DLC7_Camper_Stat2",
            "DBD_GeneratorPct_float",
            "DBD_Camper8_Stat1",
            "DBD_DLC7_Camper_Stat1",
            "DBD_Event1_Stat2",
            "DBD_Escape",
            "DBD_EscapeKO",
            "DBD_EscapeThroughHatch",
            "DBD_Chapter12_Camper_Stat2",
            "DBD_EscapeNoBlood_Obsession",
            "DBD_Camper9_Stat2",
            "DBD_HookedAndEscape",
            "DBD_DLC8_Camper_Stat1",
            "DBD_CamperNewItem",
            "DBD_CamperEscapeWithItemFrom",
            "DBD_AllEscapeThroughHatch"
        )
        val packageName = requireActivity().packageName
        for (s in statsKeysList) {
            if (StatsFragment.playerStatsMap.containsKey(s)) {
                statsList.add(
                    Stat(
                        resources.getIdentifier(s, "string", packageName),
                        StatsFragment.playerStatsMap[s].toString()
                    )
                )
            } else {
                statsList.add(
                    Stat(
                        resources.getIdentifier(s, "string", packageName),
                        "0"
                    )
                )
            }
        }
        val adapter = StatsListAdapter(
            requireContext(),
            R.layout.stats_list_item,
            statsList
        )
        survStatsListView.adapter = adapter
        return root
    }

}