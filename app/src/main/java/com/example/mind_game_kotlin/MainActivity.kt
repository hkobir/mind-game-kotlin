package com.example.mind_game_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mind_game_kotlin.models.BoardSize
import com.example.mind_game_kotlin.models.MemoryGame

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }


    private lateinit var boardRv: RecyclerView
    private lateinit var pairsTv: TextView
    private lateinit var movesTv: TextView

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryAdapter
    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init
        boardRv = findViewById(R.id.boardRV)
        pairsTv = findViewById(R.id.pairsTV)
        movesTv = findViewById(R.id.movesTV)

        memoryGame = MemoryGame(boardSize)


        adapter = MemoryAdapter(
            this,
            boardSize,
            memoryGame.cards,
            object : MemoryAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    updateGameWithFlip(position);
                }

            })

        boardRv.layoutManager = GridLayoutManager(this, boardSize.getWidth())
        boardRv.setHasFixedSize(true)
        boardRv.adapter = adapter
    }

    private fun updateGameWithFlip(position: Int) {
        memoryGame.flipCard(position)
        adapter.notifyDataSetChanged()
    }
}