package com.kodimstudio.myapplication.data

import com.kodimstudio.myapplication.model.Challenge
import com.kodimstudio.myapplication.model.Character
import com.kodimstudio.myapplication.model.Item
import com.kodimstudio.myapplication.model.Perk

class Data {
    companion object {
        val killerPerks = mutableListOf<Perk>()
        val survivorPerks = mutableListOf<Perk>()
        val killerCharacters = mutableListOf<Character>()
        val survivorCharacters = mutableListOf<Character>()
        val items = mutableListOf<Item>()
        val killerChallenges = mutableListOf<Challenge>()
        val survivorChallenges = mutableListOf<Challenge>()
    }
}
