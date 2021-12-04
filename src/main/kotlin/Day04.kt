class Day04 : Day(4, 2021, "Giant Squid") {

    private val sections = inputAsString.split("\n\n")
    private val drawn = sections.first().split(",").map { it.toInt() }
    private val boards = sections.drop(1).map { board ->
        board.split("\n").map { it.extractAllIntegers() }
    }

    private fun Board.hasBingo(check: List<Int>) =
        indices.any { idx ->
            this[idx].all { it in check } || indices.all { this[it][idx] in check }
        }

    private fun Board.score(check: List<Int>) =
        flatten().filter { it !in check }.sum() * check.last()

    override fun part1(): Int {
        val round = drawn.indices.first { n ->
            boards.any { it.hasBingo(drawn.take(n)) }
        }
        val winningNumbers = drawn.take(round)
        val winningBoard = boards.single { it.hasBingo(winningNumbers) }

        return winningBoard.score(winningNumbers)
    }

    override fun part2(): Int {
        var remainingBoards = boards
        var drawnSoFar = emptyList<Int>()
        var lastBoards = emptyList<List<List<Int>>>()
        for (n in drawn.indices) {
            drawnSoFar = drawn.take(n)
            lastBoards = remainingBoards
            remainingBoards = remainingBoards.filter { !it.hasBingo(drawnSoFar) }
            if (remainingBoards.isEmpty())
                break
        }
        return lastBoards.single().score(drawnSoFar)
    }

}

typealias Board = List<List<Int>>

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
    """.trimIndent(), 4512, 1924)
}