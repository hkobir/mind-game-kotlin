package com.example.mind_game_kotlin.models

data class MemoryCard(
    val identifier: Int,
    var isFacedUp: Boolean = false,
    var isMatched: Boolean = false
)