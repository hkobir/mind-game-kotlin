package com.example.mind_game_kotlin.models

import com.example.mind_game_kotlin.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {


    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlip = 0
    private var indexOfSelectedCard: Int? = null

    init {
        val chooseImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImage = (chooseImages + chooseImages).shuffled()
        cards = randomizedImage.map { MemoryCard(it) }
    }

    fun flipCard(position: Int): Boolean {
        numCardFlip++
        val card = cards[position]
        //Three Cases//
        //0: Card previously flipped over <> Flipped over the selected card
        //1:Card previously flipped over <> Flipped over the selected card + Check if the image matches
        //2:Card previously flipped over <> Restore cards <> Flipped over the selected card
        var foundMatched = false;
        if (indexOfSelectedCard == null) {
            //0 or 2 cards previously flipped over
            restoreCards()
            indexOfSelectedCard = position
        } else {
            //exactly 1 card previously flipped over
            foundMatched = checkFormatch(indexOfSelectedCard!!, position)
            indexOfSelectedCard = null
        }
        card.isFacedUp = !card.isFacedUp
        return foundMatched
    }

    private fun checkFormatch(indexOfSelectedCard: Int, position: Int): Boolean {
        if (cards[indexOfSelectedCard].identifier != cards[position].identifier) {
            return false
        }
        cards[indexOfSelectedCard].isMatched = true
        cards[position].isMatched = true
        numPairsFound++

        return true
    }


    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFacedUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFacedUp

    }

    fun numMoves(): Int {
        return numCardFlip / 2

    }
}
