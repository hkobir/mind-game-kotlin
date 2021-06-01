package com.example.mind_game_kotlin

import android.animation.ArgbEvaluator
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mind_game_kotlin.models.BoardSize
import com.example.mind_game_kotlin.models.MemoryGame
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }


    private lateinit var clRoot: ConstraintLayout
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
        clRoot = findViewById(R.id.clRoot)
        boardRv = findViewById(R.id.boardRV)
        pairsTv = findViewById(R.id.pairsTV)
        movesTv = findViewById(R.id.movesTV)

        setupGame();
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                if (memoryGame.numMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupGame()
                    })
                } else {

                    setupGame()
                }
                //setup the game again
            }
        }
        return true
    }

    private fun showAlertDialog(
        title: String,
        view: View?,
        positiveClickListener: View.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun setupGame() {
        when (boardSize) {
            BoardSize.EASY -> {
                movesTv.text = "Easy: 4x2"
                pairsTv.text = "Pairs: 0/4"
            }
            BoardSize.MEDIUM -> {
                movesTv.text = "Medium: 6x3"
                pairsTv.text = "Pairs: 0/9"
            }
            BoardSize.HARD -> {
                movesTv.text = "Hard: 6x4"
                pairsTv.text = "Pairs: 0/12"
            }
        }
        pairsTv.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
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
        //error checking if card already faced up
        if (memoryGame.haveWonGame()) {
            //alert the user about invalid move
            Log.i(TAG, "You already won!")
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {
            //alert the user about invalid move
            Log.i(TAG, "Invalid move!")
            Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_LONG).show()
            return
        }
        //actually flip over the card
        if (memoryGame.flipCard(position)) {
            //found a matched
            val color = ArgbEvaluator().evaluate( //value range(0-1)
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            pairsTv.setTextColor(color)
            pairsTv.text = "Pairs: ${memoryGame.numPairsFound}/${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                Snackbar.make(clRoot, "You won the game!", Snackbar.LENGTH_SHORT).show()
            }

        }
        movesTv.text = "Moves: ${memoryGame.numMoves()}"
        adapter.notifyDataSetChanged()
    }
}