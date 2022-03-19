package com.kodimstudio.myapplication.ui.stats

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.databinding.FragmentStatsBinding
import com.kodimstudio.myapplication.model.Player
import com.kodimstudio.myapplication.ui.FavouritePlayersActivity
import com.kodimstudio.myapplication.ui.MainActivity
import com.kodimstudio.myapplication.ui.stats.search.SearchPlayerFragment
import com.kodimstudio.myapplication.ui.stats.tabs.SectionsPagerAdapter
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class StatsFragment : Fragment() {
    private lateinit var playerId: String
    private lateinit var client: OkHttpClient
    private var player: Player? = null
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(binding.topBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }

            WindowInsetsCompat.CONSUMED
        }

        binding.selectPlayer.setOnClickListener { v: View? ->
            findNavController().navigate(R.id.action_stats_to_search_player)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(when(position) {
                0 -> R.string.stats_tab_text_1
                1 -> R.string.stats_tab_text_2
                2 -> R.string.stats_tab_text_3
                else -> {R.string.stats_tab_text_1}
            })
        }.attach()

        binding.retryButton.setOnClickListener { v: View? ->
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBarStats.visibility = View.VISIBLE
            binding.errorTextField.visibility = View.INVISIBLE
            binding.retryButton.visibility = View.INVISIBLE
            binding.personaName.setText(R.string.loading)
            taskFinished = false
            accountSummaries
        }

        binding.favouritePlayers.setOnClickListener {
            startActivity(Intent(requireContext(), FavouritePlayersActivity::class.java))
        }
        return root
    }


    override fun onResume() {
        super.onResume()

        binding.viewPager.visibility = View.INVISIBLE
        binding.favouritePlayers.visibility = View.INVISIBLE

        val sp = requireActivity().getSharedPreferences("stats", Context.MODE_PRIVATE)
        playerId = sp.getString("playerId", "").toString()
        client = OkHttpClient.Builder()
            .callTimeout(5, TimeUnit.SECONDS)
            .build()
        if (playerId.isEmpty()) {
            /*if (!SelectPlayerFragment.visited) {
                findNavController().navigate(R.id.action_stats_to_search_player)
            } else {
                SelectPlayerFragment.visited = false
                requireActivity().runOnUiThread { (requireActivity() as MainActivity).navController.navigateUp() }
            }*/
            if (!SearchPlayerFragment.visited) {
                findNavController().navigate(R.id.action_stats_to_search_player)
            } else {
                SearchPlayerFragment.visited = false
                findNavController().navigateUp()
            }
            return
        } else {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBarStats.visibility = View.VISIBLE
            binding.errorTextField.visibility = View.INVISIBLE
            binding.retryButton.visibility = View.INVISIBLE
            binding.personaName.setText(R.string.loading)
            taskFinished = false
            accountSummaries
        }

        val db = MainActivity.db

        if (db.playerDAO().getById(playerId.toLong()).isEmpty()) {
            binding.inFavourite.setImageResource(R.drawable.star_off)
        } else {
            binding.inFavourite.setImageResource(R.drawable.star_copy)
        }

        binding.inFavourite.setOnClickListener {
            if (db.playerDAO().getById(playerId.toLong()).isEmpty()) {
                db.playerDAO().insertAll(
                    Player(
                        steamId = playerId.toLong(),
                        nickname = player!!.nickname,
                        avatarUrl = player!!.avatarUrl
                    )
                )
                binding.inFavourite.setImageResource(R.drawable.star_copy)
            } else {
                db.playerDAO().deleteById(playerId.toLong())
                binding.inFavourite.setImageResource(R.drawable.star_off)
            }
        }
    }

    private val playerStats: Unit
        get() {
            val url: String = URLs.getPlayerStats(playerId)
            val request: Request = Request.Builder()
                .url(url)
                .build()
            Log.i("tag", url)
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        if (isAdded) {
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT)
                                    .show()
                                binding.progressBarStats.visibility = View.INVISIBLE
                                binding.errorTextField.visibility = View.VISIBLE
                                binding.retryButton.visibility = View.VISIBLE
                                binding.personaName.setText(R.string.error)
                                binding.favouritePlayers.visibility = View.VISIBLE
                            }
                        }
                        return
                    }
                    try {
                        val responseBody = response.body
                        val responseBodyStr = responseBody!!.string()
                        val playerStats = JSONObject(responseBodyStr)
                            .getJSONObject("playerstats")
                            .getJSONArray("stats")
                        playerStatsMap.clear()
                        for (i in 0 until playerStats.length()) {
                            try {
                                val stat = playerStats.getJSONObject(i)
                                val value = stat["value"]
                                if (value is Int) {
                                    playerStatsMap[stat.getString("name")] = stat["value"] as Int
                                } else if (value is Double) {
                                    playerStatsMap[stat.getString("name")] =
                                        (stat["value"] as Double).toInt()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        val achievements = JSONObject(responseBodyStr)
                            .getJSONObject("playerstats")
                            .getJSONArray("achievements")
                        playerStatsMap["DBD_TotalAchievements"] = achievements.length()
                    } catch (e: Exception) {
                        if (isAdded) {
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT)
                                    .show()
                                binding.progressBarStats.visibility = View.INVISIBLE
                                binding.errorTextField.visibility = View.VISIBLE
                                binding.retryButton.visibility = View.VISIBLE
                                binding.personaName.setText(R.string.error)
                                binding.favouritePlayers.visibility = View.VISIBLE
                            }
                        }
                        e.printStackTrace()
                        return
                    }
                    if (isAdded) {
                        requireActivity().runOnUiThread {
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.progressBarStats.visibility = View.INVISIBLE
                            binding.viewPager.visibility = View.VISIBLE

                            val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
                            binding.viewPager.adapter = sectionsPagerAdapter
                            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                                tab.setText(when(position) {
                                    0 -> R.string.stats_tab_text_1
                                    1 -> R.string.stats_tab_text_2
                                    2 -> R.string.stats_tab_text_3
                                    else -> {R.string.stats_tab_text_1}
                                })
                            }.attach()
                        }
                    }
                    taskFinished = true
                }

                override fun onFailure(call: Call, e: IOException) {
                    if (isAdded) {
                        requireActivity().runOnUiThread {
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBarStats.visibility = View.INVISIBLE
                            binding.retryButton.visibility = View.VISIBLE
                            binding.personaName.setText(R.string.error)
                            binding.favouritePlayers.visibility = View.VISIBLE
                        }
                    }
                    e.printStackTrace()
                }
            })
        }

    private val accountSummaries: Unit
        get() {
            val url: String = URLs.getPlayerSummaries(playerId)
            val request: Request = Request.Builder()
                .url(url)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        if (isAdded) {
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT)
                                    .show()
                                binding.progressBarStats.visibility = View.INVISIBLE
                                binding.retryButton.visibility = View.VISIBLE
                                binding.personaName.setText(R.string.error)
                                binding.favouritePlayers.visibility = View.VISIBLE
                            }
                        }
                        return
                    }
                    try {
                        val responseBody = response.body
                        val playerInfo = JSONObject(responseBody!!.string())
                            .getJSONObject("response")
                            .getJSONArray("players")
                            .getJSONObject(0)
                        val steamID64 = playerInfo.getString("steamid")
                        var personaName = playerInfo.getString("personaname")

                        if (personaName.length > 14) {
                            personaName = personaName.dropLast(personaName.length - 14) + "..."
                        }

                        val profileUrl = playerInfo.getString("profileurl")
                        val avatarFilename = "$steamID64.jpg"
                        val avatarUrl = playerInfo.getString("avatarfull")

                        player = Player(
                            steamId = steamID64.toLong(),
                            nickname = personaName,
                            avatarUrl = avatarUrl
                        )

                        getPlayerAvatar(avatarUrl, avatarFilename)
                        requireActivity().runOnUiThread {
                            binding.personaName.text = personaName
                        }
                    } catch (e: Exception) {
                        if (isAdded) {
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.INVISIBLE
                                Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT)
                                    .show()
                                binding.progressBarStats.visibility = View.INVISIBLE
                                binding.retryButton.visibility = View.VISIBLE
                                binding.personaName.setText(R.string.error)
                                binding.favouritePlayers.visibility = View.VISIBLE
                            }
                        }
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    if (isAdded) {
                        requireActivity().runOnUiThread {
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(context, R.string.error_occurred, Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBarStats.visibility = View.INVISIBLE
                            binding.retryButton.visibility = View.VISIBLE
                            binding.personaName.setText(R.string.error)
                            binding.favouritePlayers.visibility = View.VISIBLE
                        }
                    }
                    e.printStackTrace()
                }
            })
        }

    private fun getPlayerAvatar(avatarUrl: String, avatarFileName: String) {
        requireActivity().runOnUiThread { Picasso.get().load(avatarUrl).into(binding.avatar) }
        //requireActivity().runOnUiThread { binding.avatar.setImageBitmap(bmp) }
        playerStats
    }

    companion object {
        var taskFinished = false
        @JvmField
        val playerStatsMap = HashMap<String, Int>()
    }
}