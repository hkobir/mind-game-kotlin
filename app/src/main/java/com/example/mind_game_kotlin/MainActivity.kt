package com.example.mind_game_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mind_game_kotlin.models.BoardSize
import com.example.mind_game_kotlin.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {
    private lateinit var boardRv: RecyclerView
    private lateinit var pairsTv: TextView
    private lateinit var movesTv: TextView

    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init
        boardRv = findViewById(R.id.boardRV)
        pairsTv = findViewById(R.id.pairsTV)
        movesTv = findViewById(R.id.movesTV)

        val chooseImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImage = (chooseImages+chooseImages).shuffled()

        boardRv.layoutManager = GridLayoutManager(this, boardSize.getWidth())
        boardRv.setHasFixedSize(true)
        boardRv.adapter = MemoryAdapter(this, boardSize,randomizedImage)
    }
}