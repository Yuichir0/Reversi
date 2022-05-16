package com.example.reversi

class ReversiBack {
    private var pieceBox = mutableMapOf<Square, Char>() // Список фигур на доске
    private var whiteTurn = true // Переменная для проверки того, кто ходит
    var possibleMoves = mutableMapOf<Square, Int>() // Возможные ходы
    var whiteWin = false
    var blackWin = false
    var pat = false
    var skipTurn = false
    var blackDisks = 2
    var whiteDisks = 2

    init {
        reset()
    }

    private fun getPossibleMoves() {
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

    fun moveFromPlayer(attemptToMove: Square) {
       if (attemptToMove in possibleMoves) {
           pieceBox[Square(attemptToMove.x, attemptToMove.y)] = if (whiteTurn) 'W' else 'B'
           if (whiteTurn) whiteDisks++ else blackDisks++
           var k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать справа
               while (pieceBox[Square(attemptToMove.x + k, attemptToMove.y)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x + k, attemptToMove.y)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves[Square(attemptToMove. x, attemptToMove.y)] = possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! / 10
           k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать слева
               while (pieceBox[Square(attemptToMove.x - k, attemptToMove.y)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x - k, attemptToMove.y)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves[Square(attemptToMove. x, attemptToMove.y)] = possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! / 10
           k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать сверху
               while (pieceBox[Square(attemptToMove.x, attemptToMove.y + k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x, attemptToMove.y + k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves[Square(attemptToMove. x, attemptToMove.y)] = possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! / 10
           k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать снизу
               while (pieceBox[Square(attemptToMove.x, attemptToMove.y - k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x, attemptToMove.y - k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves[Square(attemptToMove. x, attemptToMove.y)] = possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! / 10
           k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать справа-сверху
               while (pieceBox[Square(attemptToMove.x + k, attemptToMove.y + k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x + k, attemptToMove.y + k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves[Square(attemptToMove. x, attemptToMove.y)] = possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! / 10
           k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать слева-сверху
               while (pieceBox[Square(attemptToMove.x - k, attemptToMove.y + k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x - k, attemptToMove.y + k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves[Square(attemptToMove. x, attemptToMove.y)] = possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! / 10
           k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать справа-снизу
               while (pieceBox[Square(attemptToMove.x + k, attemptToMove.y - k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x + k, attemptToMove.y - k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves[Square(attemptToMove. x, attemptToMove.y)] = possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! / 10
           k = 1
           if (possibleMoves[Square(attemptToMove. x, attemptToMove.y)]!! % 10 == 1) // Проверка надо ли переворачивать слева-снизу
               while (pieceBox[Square(attemptToMove.x - k, attemptToMove.y - k)] == if (whiteTurn) 'B' else 'W') {
                   pieceBox[Square(attemptToMove.x - k, attemptToMove.y - k)] = if (whiteTurn) 'W' else 'B'
                   if (whiteTurn) {
                       blackDisks--
                       whiteDisks++
                   } else {
                       blackDisks++
                       whiteDisks--
                   }
                   k++
               }
           possibleMoves.clear()
           skipTurn = false
           whiteTurn = !whiteTurn
           getPossibleMoves()
           if (possibleMoves.isEmpty()) {
               skipTurn = true
               whiteTurn = !whiteTurn
               getPossibleMoves()
               if (possibleMoves.isEmpty()) {
                   if (blackDisks > whiteDisks) blackWin = true
                   if (whiteDisks > blackDisks) whiteWin = true
                   if (whiteDisks == blackDisks) pat = true
               }
           }
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
        blackDisks = 2
        whiteDisks = 2
        for (i in 0..7)
            for (j in 0..7) {
                pieceBox[Square(i,j)] = '0'
            }
        pieceBox[Square(3,4)] = 'W'
        pieceBox[Square(3,3)] = 'B'
        pieceBox[Square(4,3)] = 'W'
        pieceBox[Square(4,4)] = 'B'
        getPossibleMoves()
    }

}