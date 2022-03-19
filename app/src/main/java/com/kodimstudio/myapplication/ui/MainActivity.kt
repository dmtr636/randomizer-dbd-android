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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import com.kodimstudio.myapplication.R
import com.kodimstudio.myapplication.data.AppDatabase
import com.kodimstudio.myapplication.data.Data
import com.kodimstudio.myapplication.model.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        init()
    }

    private fun init() {
        Data.killerPerks.clear()
        Data.survivorPerks.clear()
        Data.killerCharacters.clear()
        Data.survivorCharacters.clear()
        Data.items.clear()
        Data.killerChallenges.clear()
        Data.survivorChallenges.clear()

        val killerPerks = resources.getStringArray(R.array.killer_perks)
        val survivorsPerks = resources.getStringArray(R.array.survivor_perks)
        val killerCharactersStrings = resources.getStringArray(R.array.killer_characters)
        val survivorCharacters = resources.getStringArray(R.array.survivor_characters)
        val killerAddonsStringArray = resources.getStringArray(R.array.killer_addons)

        val killerAddons = hashMapOf<String, MutableList<Addon>>()

        killerAddonsStringArray.forEach { row ->
            row.split(";").also {
                if (!killerAddons.containsKey(it[0])) {
                    killerAddons[it[0]] = mutableListOf()
                }
                val resourceId = resources.getIdentifier(it[1].lowercase(), "drawable", packageName)
                killerAddons[it[0]]?.add(Addon(it[0], resourceId))
            }
        }

        killerPerks.forEach {
            val fields = it.split(";")
            val fileName = fields[0]
            val perkName = fields[1]
            val resourceId = resources.getIdentifier(fileName, "drawable", packageName)
            Data.killerPerks.add(Perk(resourceId, perkName))
        }

        survivorsPerks.forEach {
            val fields = it.split(";")
            val fileName = fields[0]
            val perkName = fields[1]
            val resourceId = resources.getIdentifier(fileName, "drawable", packageName)
            Data.survivorPerks.add(Perk(resourceId, perkName))
        }

        killerCharactersStrings.forEach { row ->
            row.split(";").also { list ->
                val characterIconFileName = list[0]
                val characterPowerFileName = list[1]
                val resourceId = resources.getIdentifier(characterIconFileName, "drawable", packageName)
                val powerResourceId = resources.getIdentifier(characterPowerFileName, "drawable", packageName)
                val selected = false
                val addons = killerAddons[characterIconFileName]!!.map {
                    it.addonIconResId
                }
                Data.killerCharacters.add(Character(resourceId, selected, addons, powerResourceId))
            }
        }

        survivorCharacters.forEach {
            val resourceId = resources.getIdentifier(it, "drawable", packageName)
            Data.survivorCharacters.add(Character(resourceId, false, listOf(), 0))
        }

        val itemsJsonString = assets.open("items.json").bufferedReader().use { it.readText() }
        val itemsJsonArray = JSONArray(itemsJsonString)
        for (i in 0 until itemsJsonArray.length()) {
            val item = itemsJsonArray.getJSONObject(i)
            val itemVariantsJsonArray = item.getJSONArray("itemVariants")
            val itemAddonsJsonArray = item.getJSONArray("itemAddons")

            val itemVariantsResIds = mutableListOf<Int>()
            val itemAddonsResIds = mutableListOf<Int>()

            for (j in 0 until itemVariantsJsonArray.length()) {
                itemVariantsResIds.add(
                    resources.getIdentifier(itemVariantsJsonArray.getString(j), "drawable", packageName)
                )
            }

            for (j in 0 until itemAddonsJsonArray.length()) {
                itemAddonsResIds.add(
                    resources.getIdentifier(itemAddonsJsonArray.getString(j), "drawable", packageName)
                )
            }

            Data.items.add(Item(itemVariantsResIds, itemAddonsResIds))
        }

        val killerChallengesStrings = resources.getStringArray(R.array.killer_challenges)
        val survivorChallengesStrings = resources.getStringArray(R.array.surv_challenges)

        killerChallengesStrings.forEachIndexed { index, s ->
            s.split(";").also {
                val id = it[0].toInt()
                val name = it[1]
                val text = it[2]
                Data.killerChallenges.add(
                    Challenge(
                        id = id,
                        name = name,
                        text = text,
                        type = "killer",
                        completed = false
                    ))
            }
        }
        survivorChallengesStrings.forEachIndexed { index, s ->
            s.split(";").also {
                val id = it[0].toInt()
                val name = it[1]
                val text = it[2]
                Data.survivorChallenges.add(
                    Challenge(
                        id = id,
                        name = name,
                        text = text,
                        type = "survivor",
                        completed = false
                    ))
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        Companion.db = db

        db.challengeDao().insertAll(Data.killerChallenges)
        db.challengeDao().insertAll(Data.survivorChallenges)

        Data.killerChallenges.forEach {
            db.challengeDao().setName(
                id = it.id,
                name = it.name,
                text = it.text
            )
        }
        Data.survivorChallenges.forEach {
            db.challengeDao().setName(
                id = it.id,
                name = it.name,
                text = it.text
            )
        }

        val sp = getSharedPreferences("challenges", Context.MODE_PRIVATE)

        if (!sp.contains("currentKillerChallengeId")) {
            sp.edit().putInt(
                "currentKillerChallengeId",
                Data.killerChallenges.random().id
            ).apply()
        }
        if (!sp.contains("currentSurvivorChallengeId")) {
            sp.edit().putInt(
                "currentKSurvivorChallengeId", Data.killerChallenges.random().id
            ).apply()
        }

        Data.killerPerks.sortBy {
            it.name
        }
        Data.survivorPerks.sortBy {
            it.name
        }
    }
}