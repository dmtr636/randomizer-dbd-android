package com.kodimstudio.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodimstudio.myapplication.adapters.SearchResultListAdapter
import com.kodimstudio.myapplication.databinding.ActivityFavouritePlayersBinding
import com.kodimstudio.myapplication.model.Player

class FavouritePlayersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritePlayersBinding
    lateinit var adapter: SearchResultListAdapter
    private lateinit var playersList: MutableList<Player>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavouritePlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //WindowCompat.setDecorFitsSystemWindows(window, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ViewCompat.setOnApplyWindowInsetsListener(binding.topBar) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            WindowInsetsCompat.CONSUMED
        }

        playersList = mutableListOf()

        val onPlayerClickListener = object : SearchResultListAdapter.OnPlayerClickListener{
            override fun onPlayerClick(player: Player, position: Int) {
                val sp = getSharedPreferences("stats", Context.MODE_PRIVATE)
                sp.edit().putString("playerId", player.steamId.toString()).commit()
                finish()
            }
        }

        adapter = SearchResultListAdapter(playersList, onPlayerClickListener)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this@FavouritePlayersActivity,
            LinearLayoutManager.VERTICAL,
            false
        )

        for (player in MainActivity.db.playerDAO().getAll()) {
            playersList.add(Player(
                    steamId = player.steamId,
                    nickname = player.nickname,
                    avatarUrl = player.avatarUrl
            ))
            adapter.notifyItemInserted(playersList.size - 1)
        }
    }
}
