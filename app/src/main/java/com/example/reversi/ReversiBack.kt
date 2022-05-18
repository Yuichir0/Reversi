package com.example.reversi

import android.util.Log
import kotlin.math.max
import kotlin.math.min

class ReversiBack {
    var pieceBox = mutableMapOf<Square, Char>() // Список фигур на доске
    var whiteTurn = true // Переменная для проверки того, кто ходит
    var possibleMoves = mutableMapOf<Square, Int>() // Возможные ходы
    var boardValue = mutableListOf(0)
    var whiteWin = false
    var blackWin = false
    var pat = false
    var skipTurn = false
    var blackDisks = mutableListOf(2)
    var whiteDisks = mutableListOf(2)
    var inCycle = false
    private var successfulMove = false
    private var pieceValue = mutableMapOf(
        Pair(Square(0,0), 1616), Pair(Square(1,0), -303), Pair(Square(2,0), 99), Pair(Square(3,0), 43), Pair(Square(4,0), 43), Pair(Square(5,0), 99), Pair(Square(6,0), -303), Pair(Square(7,0), 1616),
        Pair(Square(0,1), -412), Pair(Square(1,1), -181), Pair(Square(2,1), -8), Pair(Square(3,1), -27), Pair(Square(4,1), -27), Pair(Square(5,1), -8), Pair(Square(6,1), -181), Pair(Square(7,1), -412),
        Pair(Square(0,2), 133), Pair(Square(1,2), -4), Pair(Square(2,2), 51), Pair(Square(3,2), 7), Pair(Square(4,2), 7), Pair(Square(5,2), 51), Pair(Square(6,2), -4), Pair(Square(7,2), 133),
        Pair(Square(0,3), 63), Pair(Square(1,3), -18), Pair(Square(2,3), -4), Pair(Square(3,3), -1), Pair(Square(4,3), -1), Pair(Square(5,3), -4), Pair(Square(6,3), -18), Pair(Square(7,3), 63),
        Pair(Square(0,4), 63), Pair(Square(1,4), -18), Pair(Square(2,4), -4), Pair(Square(3,4), -1), Pair(Square(4,4), -1), Pair(Square(5,4), -4), Pair(Square(6,4), -18), Pair(Square(7,4), 63),
        Pair(Square(0,5), 133), Pair(Square(1,5), -4), Pair(Square(2,5), 51), Pair(Square(3,5), 7), Pair(Square(4,5), 7), Pair(Square(5,5), 51), Pair(Square(6,5), -4), Pair(Square(7,5), 133),
        Pair(Square(0,6), -412), Pair(Square(1,6), -181), Pair(Square(2,6), -8), Pair(Square(3,6), -27), Pair(Square(4,6), -27), Pair(Square(5,6), -8), Pair(Square(6,6), -181), Pair(Square(7,6), -412),
        Pair(Square(0,7), 1616), Pair(Square(1,7), -303), Pair(Square(2,7), 99), Pair(Square(3,7), 43), Pair(Square(4,7), 43), Pair(Square(5,7), 99), Pair(Square(6,7), -303), Pair(Square(7,7), 1616)
    )

    init {
        reset()
    }

    private fun getPossibleMoves(pieceBox: MutableMap<Square, Char>, possibleMoves: MutableMap<Square, Int>) {
        var addMove: Boolean
        var k: Int
        for (i in 0..7)
            for (j in 0..7) {
                if (pieceBox[Square(i,j)] == '0' && whiteTurn) {
                    k = 1 // Проверка вправо
                    addMove = false
                    while (i + k <= 7 && pieceBox[Square(i + k, j)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && addMove && pieceBox[Square(i + k, j)] == 'W') {
                        possibleMoves[Square(i, j)] = 1
                    }
                    k = 1 // Проверка влево
                    addMove = false
                    while (i - k >= 0 && pieceBox[Square(i - k, j)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && addMove && pieceBox[Square(i - k, j)] == 'W') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 10
                    }
                    k = 1 // Проверка вверх
                    addMove = false
                    while (j + k <= 7 && pieceBox[Square(i, j + k)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (j + k <= 7 && addMove && pieceBox[Square(i, j + k)] == 'W') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 100
                    }
                    k = 1 // Проверка вниз
                    addMove = false
                    while (j - k >= 0 && pieceBox[Square(i, j - k)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (j - k >= 0 && addMove && pieceBox[Square(i, j - k)] == 'W') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 1000
                    }
                    k = 1 // Проверка вправо-вверх
                    addMove = false
                    while (i + k <= 7 && j + k <= 7 && pieceBox[Square(i + k, j + k)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && j + k <= 7 && addMove && pieceBox[Square(i + k, j + k)] == 'W') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 10000
                    }
                    k = 1 // Проверка влево-вверх
                    addMove = false
                    while (i - k >= 0 && j + k <= 7 && pieceBox[Square(i - k, j + k)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && j + k <= 7 && addMove && pieceBox[Square(i - k, j + k)] == 'W') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 100000
                    }
                    k = 1 // Проверка вправо-вниз
                    addMove = false
                    while (i + k <= 7 && j - k >= 0 && pieceBox[Square(i + k, j - k)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && j - k >= 0 && addMove && pieceBox[Square(i + k, j - k)] == 'W') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 1000000
                    }
                    k = 1 // Проверка влево-вниз
                    addMove = false
                    while (i - k >= 0 && j - k >= 0 && pieceBox[Square(i - k, j - k)] == 'B') {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && j - k >= 0 && addMove && pieceBox[Square(i - k, j - k)] == 'W') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 10000000
                    }
                }

                if (pieceBox[Square(i,j)] == '0' && !whiteTurn) {
                    k = 1 // Проверка вправо
                    addMove = false
                    while (i + k <= 7 && pieceBox[Square(i + k, j)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && addMove && pieceBox[Square(i + k, j)] == 'B') {
                        possibleMoves[Square(i, j)] = 1
                    }
                    k = 1 // Проверка влево
                    addMove = false
                    while (i - k >= 0 && pieceBox[Square(i - k, j)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && addMove && pieceBox[Square(i - k, j)] == 'B') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 10
                    }
                    k = 1 // Проверка вверх
                    addMove = false
                    while (j + k <= 7 && pieceBox[Square(i, j + k)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (j + k <= 7 && addMove && pieceBox[Square(i, j + k)] == 'B') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 100
                    }
                    k = 1 // Проверка вниз
                    addMove = false
                    while (j - k >= 0 && pieceBox[Square(i, j - k)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (j - k >= 0 && addMove && pieceBox[Square(i, j - k)] == 'B') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 1000
                    }
                    k = 1 // Проверка вправо-вверх
                    addMove = false
                    while (i + k <= 7 && j + k <= 7 && pieceBox[Square(i + k, j + k)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && j + k <= 7 && addMove && pieceBox[Square(i + k, j + k)] == 'B') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 10000
                    }
                    k = 1 // Проверка влево-вверх
                    addMove = false
                    while (i - k >= 0 && j + k <= 7 && pieceBox[Square(i - k, j + k)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && j + k <= 7 && addMove && pieceBox[Square(i - k, j + k)] == 'B') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 100000
                    }
                    k = 1 // Проверка вправо-вниз
                    addMove = false
                    while (i + k <= 7 && j - k >= 0 && pieceBox[Square(i + k, j - k)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (i + k <= 7 && j - k >= 0 && addMove && pieceBox[Square(i + k, j - k)] == 'B') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 1000000
                    }
                    k = 1 // Проверка влево-вниз
                    addMove = false
                    while (i - k >= 0 && j - k >= 0 && pieceBox[Square(i - k, j - k)] == 'W') {
                        addMove = true
                        k++
                    }
                    if (i - k >= 0 && j - k >= 0 && addMove && pieceBox[Square(i - k, j - k)] == 'B') {
                        possibleMoves[Square(i, j)] = possibleMoves.getOrElse(Square(i, j)) {0} + 10000000
                    }
                }
            }
    }

    fun moveFromPlayer(attemptToMove: Square, pieceBox: MutableMap<Square, Char>, possibleMoves: MutableMap<Square, Int>, whiteTurn: Boolean, blackDisks: MutableList<Int>, whiteDisks: MutableList<Int>, boardValue: MutableList<Int>) {
       if (attemptToMove in possibleMoves) {
           successfulMove = true
           val x = attemptToMove.x
           val y = attemptToMove.y
           pieceBox[Square(x, y)] = if (whiteTurn) 'W' else 'B'
           if (whiteTurn) {
               whiteDisks[0]++
               boardValue[0] += pieceValue[Square(x, y)]!!
           } else {
               blackDisks[0]++
               boardValue[0] -= pieceValue[Square(x, y)]!!
           }
           var k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать справа
               while (pieceBox[Square(x + k, y)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x + k, y)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x + k, y)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x + k, y)]!!
                   }
                   k++
               }
           possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
           k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать слева
               while (pieceBox[Square(x - k, y)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x - k, y)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x - k, y)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x - k, y)]!!
                   }
                   k++
               }
           possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
           k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать сверху
               while (pieceBox[Square(x, y + k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x, y + k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x, y + k)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x, y + k)]!!
                   }
                   k++
               }
           possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
           k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать снизу
               while (pieceBox[Square(x, y - k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x, y - k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x, y - k)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x, y - k)]!!
                   }
                   k++
               }
           possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
           k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать справа-сверху
               while (pieceBox[Square(x + k, y + k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x + k, y + k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x + k, y + k)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x + k, y + k)]!!
                   }
                   k++
               }
           possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
           k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать слева-сверху
               while (pieceBox[Square(x - k, y + k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x - k, y + k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x - k, y + k)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x - k, y + k)]!!
                   }
                   k++
               }
           possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
           k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать справа-снизу
               while (pieceBox[Square(x + k, y - k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x + k, y - k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x + k, y - k)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x + k, y - k)]!!
                   }
                   k++
               }
           possibleMoves[Square(x, y)] = possibleMoves[Square(x, y)]!! / 10
           k = 1
           if (possibleMoves[Square(x, y)]!! % 10 == 1) // Проверка надо ли переворачивать слева-снизу
               while (pieceBox[Square(x - k, y - k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(x - k, y - k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks[0]--
                       whiteDisks[0]++
                       boardValue[0] += 2 * pieceValue[Square(x - k, y - k)]!!
                   } else {
                       blackDisks[0]++
                       whiteDisks[0]--
                       boardValue[0] -= 2 * pieceValue[Square(x - k, y - k)]!!
                   }
                   k++
               }
           possibleMoves.clear()
           skipTurn = false
           this.whiteTurn = !whiteTurn
           getPossibleMoves(pieceBox, possibleMoves)
           if (possibleMoves.isEmpty()) {
               skipTurn = true
               this.whiteTurn = !whiteTurn
               getPossibleMoves(pieceBox, possibleMoves)
               if (possibleMoves.isEmpty()) {
                   if (blackDisks[0] > whiteDisks[0]) {
                       if (!inCycle) blackWin = true
                   }
                   if (whiteDisks[0] > blackDisks[0]) {
                       if (!inCycle) whiteWin = true
                   }
                   if (whiteDisks[0] == blackDisks[0]) {
                       if (!inCycle) pat = true
                   }
               }
           }
        } else successfulMove = false
    }

    fun moveFromAI() {
        if (successfulMove || skipTurn) {
            inCycle = true
            val rememberWhiteTurn = whiteTurn
            var bestMove = Square(-1, -1)
            var bestScore = if (rememberWhiteTurn) Int.MIN_VALUE else Int.MAX_VALUE
            for (move in possibleMoves) {
                val newPieceBox = mutableMapOf<Square, Char>()
                for (piece in pieceBox)
                    newPieceBox[piece.key] = piece.value
                val newPossibleMoves = mutableMapOf<Square, Int>()
                for (possibleMove in possibleMoves)
                    newPossibleMoves[possibleMove.key] = possibleMove.value
                val newWhiteDisks = mutableListOf<Int>()
                for (disk in whiteDisks)
                    newWhiteDisks.add(disk)
                val newBlackDisks = mutableListOf<Int>()
                for (disk in blackDisks)
                    newBlackDisks.add(disk)
                val newBoardValue = mutableListOf<Int>()
                for (value in boardValue)
                    newBoardValue.add(value)
                moveFromPlayer(move.key, newPieceBox, newPossibleMoves, rememberWhiteTurn, newBlackDisks, newWhiteDisks, newBoardValue)
                val scoreAI = minimax(pieceBox, possibleMoves, whiteDisks, blackDisks, boardValue, 6, Int.MIN_VALUE, Int.MAX_VALUE, whiteTurn)
                if (rememberWhiteTurn) if (scoreAI > bestScore) {
                    bestScore = scoreAI
                    bestMove = move.key
                }
                if (!rememberWhiteTurn) if (scoreAI < bestScore) {
                    bestScore = scoreAI
                    bestMove = move.key
                }
            }
            Log.d(TAG, "moveAI = $bestScore, $bestMove")
            whiteTurn = rememberWhiteTurn
            inCycle = false
            moveFromPlayer(bestMove, pieceBox, possibleMoves, whiteTurn, blackDisks, whiteDisks, boardValue)
        }
    }

    private fun minimax(pieceBox: MutableMap<Square, Char>, possibleMoves: MutableMap<Square, Int>, whiteDisks: MutableList<Int>, blackDisks: MutableList<Int>, boardValue: MutableList<Int>, depth: Int, alpha: Int, beta: Int, whiteTurn: Boolean): Int {
        var bestScore: Int
        var newAlpha = alpha
        var newBeta = beta
        if (depth == 0 || whiteWin || blackWin || pat) return boardValue[0]
        if (whiteTurn) {
            bestScore = Int.MIN_VALUE
            for (move in possibleMoves.keys) {
                val newPieceBox = mutableMapOf<Square, Char>()
                for (piece in pieceBox)
                    newPieceBox[piece.key] = piece.value
                val newPossibleMoves = mutableMapOf<Square, Int>()
                for (possibleMove in possibleMoves)
                    newPossibleMoves[possibleMove.key] = possibleMove.value
                val newWhiteDisks = mutableListOf<Int>()
                for (disk in whiteDisks)
                    newWhiteDisks.add(disk)
                val newBlackDisks = mutableListOf<Int>()
                for (disk in blackDisks)
                    newBlackDisks.add(disk)
                val newBoardValue = mutableListOf<Int>()
                for (value in boardValue)
                    newBoardValue.add(value)
                moveFromPlayer(move, newPieceBox, newPossibleMoves, false, newBlackDisks, newWhiteDisks, newBoardValue)
                val score = minimax(newPieceBox, newPossibleMoves, newWhiteDisks, newBlackDisks, newBoardValue, depth - 1, newAlpha, newBeta, false)
                if (score >= bestScore) {
                    bestScore = score
                }
                newAlpha = max(alpha, score)
                if (newBeta <= newAlpha) break
            }
            return bestScore
        } else {
            bestScore = Int.MAX_VALUE
            for (move in possibleMoves.keys) {
                val newPieceBox = mutableMapOf<Square, Char>()
                for (piece in pieceBox)
                    newPieceBox[piece.key] = piece.value
                val newPossibleMoves = mutableMapOf<Square, Int>()
                for (possibleMove in possibleMoves)
                    newPossibleMoves[possibleMove.key] = possibleMove.value
                val newWhiteDisks = mutableListOf<Int>()
                for (disk in whiteDisks)
                    newWhiteDisks.add(disk)
                val newBlackDisks = mutableListOf<Int>()
                for (disk in blackDisks)
                    newBlackDisks.add(disk)
                val newBoardValue = mutableListOf<Int>()
                for (value in boardValue)
                    newBoardValue.add(value)
                moveFromPlayer(move, newPieceBox, newPossibleMoves, true, newBlackDisks, newWhiteDisks, newBoardValue)
                val score = minimax(newPieceBox, newPossibleMoves, newWhiteDisks, newBlackDisks, newBoardValue, depth - 1, newAlpha, newBeta, true)
                if (score <= bestScore) {
                    bestScore = score
                }
                newBeta = min(beta, score)
                if (newBeta <= newAlpha) break
            }
            return bestScore
        }
    }

    fun square(x: Int, y: Int): Char? {
        return pieceBox[Square(x,y)]
    }

    fun reset() {
        whiteTurn = true
        pieceBox.clear()
        possibleMoves.clear()
        blackWin = false
        whiteWin = false
        pat = false
        skipTurn = false
        blackDisks[0] = 2
        whiteDisks[0] = 2
        boardValue[0] = 0
        for (i in 0..7)
            for (j in 0..7) {
                pieceBox[Square(i,j)] = '0'
            }
        pieceBox[Square(3,4)] = 'W'
        pieceBox[Square(3,3)] = 'B'
        pieceBox[Square(4,3)] = 'W'
        pieceBox[Square(4,4)] = 'B'
        getPossibleMoves(pieceBox, possibleMoves)
    }
}