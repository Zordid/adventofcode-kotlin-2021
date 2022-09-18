class Day04 : Day(4, 2021, "Giant Squid") {

    private val sections = inputAsString.split("\n\n")
    private val drawn = sections.first().split(",").map { it.toInt() }
    private val boards = sections.drop(1).map { board ->
        Matrix(board.split("\n").map { it.extractAllIntegers() })
    }

    private val drawnInRound = (0..drawn.size).associateWith { round ->
        drawn.take(round).toSet()
    }

    private fun Board.hasBingo(drawnNumbers: Collection<Int>): Boolean =
        (rows + columns).any { (it - drawnNumbers.toSet()).isEmpty() }

    private fun Board.score(check: Collection<Int>, lastDrawn: Int) =
        flatten().filter { it !in check }.sum() * lastDrawn

    fun Board.numbersNeededForBingo(numbers: List<Int>) = numbers.indices.firstOrNull {
        hasBingo(numbers.take(it))
    }


    override fun part1(): Int {
        val round = drawn.indices.first { n ->
            boards.any { it.hasBingo(drawnInRound[n]!!) }
        }
        val winningNumbers = drawnInRound[round]!!
        val winningBoard = boards.single { it.hasBingo(winningNumbers) }

        return winningBoard.score(winningNumbers, drawn[round - 1])
    }

    override fun part2(): Int {
//        return boards.map { it to (it.numbersNeededForBingo(drawn) ?: 0) }.maxByOrNull { it.second }!!.let { (b,n)->
//            b.score(drawn.slice(0 until n))
//        }
        var remainingBoards = boards
        var drawnSoFar = emptySet<Int>()
        var lastBoards = emptyList<Board>()
        for (n in 0..drawn.size) {
            drawnSoFar = drawnInRound[n]!!
            lastBoards = remainingBoards
            remainingBoards = remainingBoards.filter { !it.hasBingo(drawnSoFar) }
            if (remainingBoards.isEmpty())
                break
        }
        return lastBoards.single().score(drawnSoFar, drawn[drawnSoFar.size - 1])
    }

}

typealias Board = Matrix<Int>

fun main() {
    solve<Day04>(
        """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
    """.trimIndent(), 4512, 1924
    )
}

data class Matrix<T>(val rows: List<List<T>>) : List<List<T>> by rows {
    init {
        require(rows.all { it.size == rows.first().size })
    }

    val columns: List<List<T>> by lazy {
        rows.first().indices.map { col -> rows.map { it[col] } }
    }

    fun transposed(): Matrix<T> = Matrix(columns)
    fun flippedHorizontally(): Matrix<T> = Matrix(rows.reversed())
    fun flippedVertically(): Matrix<T> = Matrix(rows.map { it.reversed() })

    fun forEachIndexed(action: (rowIdx: Int, colIdx: Int, value: T) -> Unit) {
        forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, v -> action(rowIdx, colIdx, v) }
        }
    }

    fun <R> map(transform: (value: T) -> R): Matrix<R> = Matrix(rows.map { row -> row.map { transform(it) } })
    fun <R> mapIndexed(transform: (rowIdx: Int, colIdx: Int, value: T) -> R): Matrix<R> =
        Matrix(rows.mapIndexed { rowIdx, row -> row.mapIndexed { colIdx, v -> transform(rowIdx, colIdx, v) } })

    override fun toString(): String {
        return rows.joinToString(System.lineSeparator())
    }
}