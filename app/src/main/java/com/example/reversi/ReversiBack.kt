package com.example.reversi

import android.util.Log
import kotlin.math.max
import kotlin.math.min

class ReversiBack {
    val pieceBox = mutableMapOf<Square, Char>() // Список фигур на доске
    var whiteTurn = true // Переменная для проверки того, кто ходит
    val possibleMoves = mutableMapOf<Square, Int>() // Возможные ходы
    val boardValue = mutableListOf(0)
    private var whiteWin = false
    private var blackWin = false
    private var pat = false
    var announceWhiteWin = false
    var announceBlackWin = false
    var announcePat = false
    var skipTurn = false
    var doubleSkipTurn = false
    val blackDisks = mutableListOf(2)
    val whiteDisks = mutableListOf(2)
    private var inCycle = false
    private var successfulMove = false
    private val pieceValue = mutableMapOf(
        Pair(Square(0,0), 20000), Pair(Square(1,0), -3000), Pair(Square(2,0), 1000), Pair(Square(3,0), 800), Pair(Square(4,0), 800), Pair(Square(5,0), 1000), Pair(Square(6,0), -3000), Pair(Square(7,0), 20000),
        Pair(Square(0,1), -3000), Pair(Square(1,1), -5000), Pair(Square(2,1), -450), Pair(Square(3,1), -500), Pair(Square(4,1), -500), Pair(Square(5,1), -450), Pair(Square(6,1), -5000), Pair(Square(7,1), -3000),
        Pair(Square(0,2), 1000), Pair(Square(1,2), -450), Pair(Square(2,2), 30), Pair(Square(3,2), 10), Pair(Square(4,2), 10), Pair(Square(5,2), 30), Pair(Square(6,2), -450), Pair(Square(7,2), 1000),
        Pair(Square(0,3), 800), Pair(Square(1,3), -500), Pair(Square(2,3), 10), Pair(Square(3,3), 50), Pair(Square(4,3), 50), Pair(Square(5,3), 10), Pair(Square(6,3), -500), Pair(Square(7,3), 800),
        Pair(Square(0,4), 800), Pair(Square(1,4), -500), Pair(Square(2,4), 10), Pair(Square(3,4), 50), Pair(Square(4,4), 50), Pair(Square(5,4), 10), Pair(Square(6,4), -500), Pair(Square(7,4), 800),
        Pair(Square(0,5), 1000), Pair(Square(1,5), -450), Pair(Square(2,5), 30), Pair(Square(3,5), 10), Pair(Square(4,5), 10), Pair(Square(5,5), 30), Pair(Square(6,5), -450), Pair(Square(7,5), 1000),
        Pair(Square(0,6), -3000), Pair(Square(1,6), -5000), Pair(Square(2,6), -450), Pair(Square(3,6), -500), Pair(Square(4,6), -500), Pair(Square(5,6), -450), Pair(Square(6,6), -5000), Pair(Square(7,6), -3000),
        Pair(Square(0,7), 20000), Pair(Square(1,7), -3000), Pair(Square(2,7), 1000), Pair(Square(3,7), 800), Pair(Square(4,7), 800), Pair(Square(5,7), 1000), Pair(Square(6,7), -3000), Pair(Square(7,7), 20000),
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

    fun moveFromPlayer(attemptToMove: Square, pieceBox: MutableMap<Square, Char>, possibleMoves: MutableMap<Square, Int>, whiteTurn: Boolean, blackDisks: MutableList<Int>, whiteDisks: MutableList<Int>, boardValue: MutableList<Int>, lastMove: Boolean) {
       if (skipTurn) {
           this.whiteTurn = !whiteTurn
           getPossibleMoves(pieceBox, possibleMoves)
       }
        if (attemptToMove in possibleMoves) {
           whiteWin = false
           blackWin = false
           pat = false
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
            doubleSkipTurn = skipTurn
           skipTurn = false
           this.whiteTurn = !whiteTurn
           getPossibleMoves(pieceBox, possibleMoves)
           if (possibleMoves.isEmpty()) {
               skipTurn = true
               this.whiteTurn = !whiteTurn
               getPossibleMoves(pieceBox, possibleMoves)
               if (whiteDisks[0] + blackDisks[0] == 64) {
                   if (blackDisks[0] > whiteDisks[0]) {
                       if (lastMove) announceBlackWin = true
                       blackWin = true
                   }
                   if (whiteDisks[0] > blackDisks[0]) {
                       if (lastMove) announceWhiteWin = true
                       whiteWin = true
                   }
                   if (whiteDisks[0] == blackDisks[0]) {
                       if (lastMove) announcePat = true
                       pat = true
                   }
               }
           }
        } else successfulMove = false
    }

    fun moveFromAI() {
        Log.d(TAG, "successfulMove = $successfulMove, skipTurn = $skipTurn, whiteTurn = $whiteTurn")
        if (skipTurn) {
            this.whiteTurn = !whiteTurn
            getPossibleMoves(pieceBox, possibleMoves)
        }
        if (successfulMove && !skipTurn || !successfulMove && skipTurn) {
            if (skipTurn) {
                whiteTurn = !whiteTurn
            }
            inCycle = true
            val rememberWhiteTurn = whiteTurn
            var bestMove = if (possibleMoves.isNotEmpty()) possibleMoves.keys.first() else Square(-1, -1)
            var bestScore = if (rememberWhiteTurn) Int.MIN_VALUE else Int.MAX_VALUE
            for (move in possibleMoves) {
                val newPieceBox = mutableMapOf<Square, Char>()
                for (piece in pieceBox)
                    newPieceBox[piece.key] = piece.value
                val newPossibleMoves = mutableMapOf<Square, Int>()
                for (possibleMove in possibleMoves)
                    newPossibleMoves[possibleMove.key] = possibleMove.value
                val newWhiteDisks = mutableListOf<Int>()
                newWhiteDisks.add(whiteDisks[0])
                val newBlackDisks = mutableListOf<Int>()
                newBlackDisks.add(blackDisks[0])
                val newBoardValue = mutableListOf<Int>()
                newBoardValue.add(boardValue[0])
                moveFromPlayer(move.key, newPieceBox, newPossibleMoves, rememberWhiteTurn, newBlackDisks, newWhiteDisks, newBoardValue, false)
                val scoreAI = minimax(pieceBox, possibleMoves, whiteDisks, blackDisks, boardValue, 5, Int.MIN_VALUE, Int.MAX_VALUE, whiteTurn)
                if (rememberWhiteTurn) if (scoreAI > bestScore) {
                    bestScore = scoreAI
                    bestMove = move.key
                    break
                }
                if (!rememberWhiteTurn) if (scoreAI < bestScore) {
                    bestScore = scoreAI
                    bestMove = move.key
                    break
                }
            }
            Log.d(TAG, "moveAI = $bestScore, $bestMove")
                   whiteTurn = rememberWhiteTurn
            inCycle = false
            moveFromPlayer(bestMove, pieceBox, possibleMoves, whiteTurn, blackDisks, whiteDisks, boardValue, true)
        }
    }

    private fun minimax(pieceBox: MutableMap<Square, Char>, possibleMoves: MutableMap<Square, Int>, whiteDisks: MutableList<Int>, blackDisks: MutableList<Int>, boardValue: MutableList<Int>, depth: Int, alpha: Int, beta: Int, whiteTurn: Boolean): Int {
        var bestScore: Int
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
                newWhiteDisks.add(whiteDisks[0])
                val newBlackDisks = mutableListOf<Int>()
                newBlackDisks.add(blackDisks[0])
                val newBoardValue = mutableListOf<Int>()
                newBoardValue.add(boardValue[0])
                moveFromPlayer(move, newPieceBox, newPossibleMoves, false, newBlackDisks, newWhiteDisks, newBoardValue, false)
                val score = minimax(newPieceBox, newPossibleMoves, newWhiteDisks, newBlackDisks, newBoardValue, depth - 1, alpha, beta, false)
                if (score >= bestScore) {
                    bestScore = score
                }
                val newAlpha = max(alpha, bestScore)
             //   Log.d(TAG, "alpha = $alpha, beta = $beta")
                if (beta < newAlpha) break
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
                newWhiteDisks.add(whiteDisks[0])
                val newBlackDisks = mutableListOf<Int>()
                newBlackDisks.add(blackDisks[0])
                val newBoardValue = mutableListOf<Int>()
                newBoardValue.add(boardValue[0])
                moveFromPlayer(move, newPieceBox, newPossibleMoves, true, newBlackDisks, newWhiteDisks, newBoardValue, false)
                val score = minimax(newPieceBox, newPossibleMoves, newWhiteDisks, newBlackDisks, newBoardValue, depth - 1, alpha, beta, true)
                if (score <= bestScore) {
                    bestScore = score
                }
                val newBeta = min(beta, bestScore)
            //    Log.d(TAG, "alpha = $alpha, beta = $beta")
                if (newBeta < alpha) break
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
        announceBlackWin = false
        announcePat = false
        announceWhiteWin = false
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