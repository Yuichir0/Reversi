package com.example.reversi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ReversiConnector {

    private val reversiBack = ReversiBack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "$reversiBack")
        val reversiFront: ReversiFront = findViewById(R.id.chess_view)
        reversiFront.reversiConnector = this
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            reversiBack.reset()
            reversiFront.invalidate()
        }
    }

    override fun square(x: Int, y: Int): Char? {
        return reversiBack.square(x, y)
    }

    override fun moveFromPlayer(moveFromPlayer: Square) {
        reversiBack.moveFromPlayer(Square(moveFromPlayer.x, moveFromPlayer.y))
        if (reversiBack.whiteWin) Toast.makeText(applicationContext, "White win! \nBlack Disks = ${reversiBack.blackDisks}, White Disks = ${reversiBack.whiteDisks}", Toast.LENGTH_LONG).show()
        if (reversiBack.blackWin) Toast.makeText(applicationContext, "Black win! \nBlack Disks = ${reversiBack.blackDisks}, White Disks = ${reversiBack.whiteDisks}", Toast.LENGTH_LONG).show()
        if (reversiBack.pat) Toast.makeText(applicationContext, "Everyone lost! \nBlack Disks = ${reversiBack.blackDisks}, White Disks = ${reversiBack.whiteDisks}", Toast.LENGTH_LONG).show()
        if (!reversiBack.whiteWin && !reversiBack.blackWin && !reversiBack.pat && reversiBack.skipTurn) Toast.makeText(applicationContext, "No possible moves, skip turn", Toast.LENGTH_SHORT).show()
        findViewById<ReversiFront>(R.id.chess_view).invalidate()
    }

    override fun returnPossibleMoves(): MutableMap<Square, Int> {
        return reversiBack.possibleMoves
    }
}