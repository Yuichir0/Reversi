package com.example.reversi

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import java.lang.Integer.min

class ReversiFront(context: Context?, attrs: AttributeSet?): View(context, attrs) {
    private var startX = 20f
    private var startY = 180f
    private var squareSize = 130f
    private val piecesImages = setOf(
        R.drawable.bd,
        R.drawable.wd,
        R.drawable.pt
    )
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val color = Paint()
    var reversiConnector: ReversiConnector? = null

    init {
        loadBitmap()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(min, min)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDraw(canvas: Canvas) {
        val chessBoardSide = min(width, height) * 0.95
        squareSize = (chessBoardSide / 8).toFloat()
        startX = ((width - chessBoardSide) / 2).toFloat()
        startY = ((height - chessBoardSide) / 2).toFloat()

        createChessBoard(canvas)
        loadPieces(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                    val moveX = ((event.x - startX) / squareSize).toInt()
                    val moveY = 7 - ((event.y - startY) / squareSize).toInt()
                    reversiConnector?.moveFromPlayer(Square(moveX, moveY))
                    findViewById<ReversiFront>(R.id.chess_view).invalidate()
                }
            MotionEvent.ACTION_UP -> {
                reversiConnector?.moveFromAI()
                findViewById<ReversiFront>(R.id.chess_view).invalidate()
            }
            }
        return true
    }

    private fun loadPieces(canvas: Canvas) {
        for (row in 0..7)
            for (column in 0..7) {
                if (reversiConnector?.square(column, row) != '0') {
                    val frontPieceType: Int = if (reversiConnector?.square(column, row) == 'W') R.drawable.wd else R.drawable.bd
                    loadPieceAt(canvas, column, row, frontPieceType)
                }
                reversiConnector!!.returnPossibleMoves().forEach {
                    loadPieceAt(canvas, it.key.x, it.key.y, R.drawable.pt)
                }
            }
    }

    private fun loadPieceAt(canvas: Canvas, column: Int, row: Int, pieceName: Int) {
        val loadingPiece = bitmaps[pieceName]!!
        canvas.drawBitmap(loadingPiece, null, RectF(startX + column * squareSize + 10, startY + (7 - row) * squareSize + 10, startX + (column + 1) * squareSize - 10, startY + (8 - row) * squareSize - 10), color)
    }

    private fun loadBitmap() {
        piecesImages.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun createChessBoard(canvas: Canvas) {
        color.color = Color.argb(255, 125, 148, 93)
        canvas.drawRect(startX, startY, startX + 8 * squareSize, startY + 8 * squareSize, color)
        color.color = Color.argb(255, 0, 0, 0)
        color.strokeWidth = 6F
        for (i in 1..8) {
            canvas.drawLine(
                startX + i * squareSize,
                startY,
                startX + i * squareSize,
                startY + 8 * squareSize,
                color
            )
            canvas.drawLine(
                startX,
                startY + i * squareSize,
                startX + 8 * squareSize,
                startY + i * squareSize,
                color
            )
        }
    }
}