class Day04 : Day(4, 2021) {

    val p = input.joinToString("\n").split("\n\n")
    val drawn = p.first().split(",").map { it.toInt() }
    val boards = p.drop(1).map {
        it.split("\n").map { board ->
            board.trim().split("""\s+""".toRegex()).map { it.toInt() }
        }
    }

    fun List<List<Int>>.hasRow(check: List<Int>) =
        any { row ->
            row.all { it in check }.also { if (it) println("Bingo in row $row") }
        }

    fun List<List<Int>>.hasCol(check: List<Int>) =
        indices.any { col ->
            indices.all { row -> this[row][col] in check }.also { if (it) println("Bingo in col $col") }
        }

    fun List<List<Int>>.hasDiagonal1(check: List<Int>) =
        indices.all { idx ->
            this[idx][idx] in check
        }.also { if (it) println("Bingo in diagonal 1") }

    fun List<List<Int>>.hasDiagonal2(check: List<Int>) =
        indices.all { idx ->
            this[lastIndex - idx][idx] in check
        }.also { if (it) println("Bingo in diagonal 2") }

    fun List<List<Int>>.hasBingo(check: List<Int>) =
        hasRow(check) || hasCol(check) //|| hasDiagonal1(check) || hasDiagonal2(check)

    override fun part1(): Any? {
        val win = drawn.indices.first { n ->
            println("Checking ${drawn.take(n)}")
            boards.any { it.hasBingo(drawn.take(n)) }.also { println("Winner: $it") }
        }
        println(win)
        val winningNumbers = drawn.take(win)
        println(winningNumbers)
        val b = boards.single { it.hasBingo(winningNumbers) }
        b.forEach { println(it) }
        val sum = b.flatten().filter { it !in winningNumbers }.sum()
        return sum * winningNumbers.last()
    }

    override fun part2(): Any? {
        var remainingBoards = boards
        var drawnSoFar = emptyList<Int>()
        var lastBoards = emptyList<List<List<Int>>>()
        for(n in drawn.indices) {
            drawnSoFar = drawn.take(n)
            lastBoards = remainingBoards
            remainingBoards = remainingBoards.filter { !it.hasBingo(drawnSoFar) }
            if (remainingBoards.isEmpty())
                break
        }
        val b = lastBoards.single()
        b.forEach { println(it) }
        val sum = b.flatten().filter { it !in drawnSoFar }.sum()
        return sum * drawnSoFar.last()
    }

}

fun main() {
    solve<Day04>("""
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
    """.trimIndent(),
        4512,
        1924)
}