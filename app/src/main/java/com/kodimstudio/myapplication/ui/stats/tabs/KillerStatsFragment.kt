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

class KillerStatsFragment : Fragment() {
    private lateinit var killerStatsListView: ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_killer_stats, container, false)
        killerStatsListView = root.findViewById(R.id.killerStatsListView)
        val statsList: MutableList<Stat> = ArrayList()
        val statsKeysList = arrayOf(
            "DBD_SlasherMaxScoreByCategory",
            "DBD_SlasherFullLoadout",
            "DBD_SacrificedCampers",
            "DBD_KilledCampers",
            "DBD_Chapter11_Slasher_Stat1",
            "DBD_DLC8_Slasher_Stat2",
            "DBD_DLC7_Slasher_Stat2",
            "DBD_DLC6_Slasher_Stat2",
            "DBD_Event1_Stat1",
            "DBD_Chapter14_Slasher_Stat1",
            "DBD_Chapter18_Slasher_Stat2",
            "DBD_Chapter20_Slasher_Stat2",
            "DBD_DLC9_Slasher_Stat1",
            "DBD_Chapter10_Slasher_Stat1",
            "DBD_Chapter12_Slasher_Stat1",
            "DBD_Chapter13_Slasher_Stat1",
            "DBD_Chapter9_Slasher_Stat1",
            "DBD_Chapter16_Slasher_Stat2",
            "DBD_Chapter17_Slasher_Stat2",
            "DBD_Chapter19_Slasher_Stat2",
            "DBD_DLC6_Slasher_Stat1",
            "DBD_ChainsawHit",
            "DBD_DLC9_Slasher_Stat2",
            "DBD_DLC5_Slasher_Stat2",
            "DBD_Chapter12_Slasher_Stat2",
            "DBD_Chapter11_Slasher_Stat2",
            "DBD_Chapter13_Slasher_Stat2",
            "DBD_Chapter14_Slasher_Stat2",
            "DBD_Chapter9_Slasher_Stat2",
            "DBD_Chapter10_Slasher_Stat2",
            "DBD_Chapter15_Slasher_Stat1",
            "DBD_Chapter18_Slasher_Stat1",
            "DBD_Chapter20_Slasher_Stat1"
        )
        val packageName = requireActivity().packageName
        for (i in statsKeysList.indices) {
            if (StatsFragment.playerStatsMap.containsKey(statsKeysList[i])) {
                statsList.add(
                    Stat(
                        resources.getIdentifier(statsKeysList[i], "string", packageName),
                        StatsFragment.playerStatsMap[statsKeysList[i]].toString()
                    )
                )
            } else {
                statsList.add(
                    Stat(
                        resources.getIdentifier(statsKeysList[i], "string", packageName),
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
        killerStatsListView.adapter = adapter
        return root
    }

}