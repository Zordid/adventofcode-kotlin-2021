class Day21 : Day(21, 2021, "Dirac Dice") {

    val start = mappedInput { it.extractAllIntegers().last() }

    override fun part1(): Int {
        val positions = start.toMutableList()
        val scores = mutableListOf(0, 0)

        var diceNext = 1
        var rolled = 0

        fun roll() = diceNext.also {
            diceNext = (diceNext % 1000) + 1
            rolled++
        }

        while (true) {
            for (player in 0..1) {
                val diceTotal = roll() + roll() + roll()
                positions[player] = (positions[player] - 1 + diceTotal) % 10 + 1
                scores[player] += positions[player]
                if (scores[player] >= 1000) {
                    return rolled * scores[(player + 1) % 2]
                }
            }
        }
    }

    override fun part2(): Long {
        val c = (1..3).flatMap { a ->
            (1..3).flatMap { b ->
                (1..3).map { c ->
                    listOf(a, b, c)
                }
            }
        }
        val universeStatistics = c.groupingBy { it.sum() }.eachCount()
        val wins = LongArray(2)

        fun game(player: Int, universes: Long, positions: IntArray, scores: IntArray = IntArray(2)) {
            val pos = positions[player]
            val score = scores[player]
            for ((diceTotal, numberOfUniverses) in universeStatistics) {
                val newPos = (pos + diceTotal - 1) % 10 + 1
                val newScore = score + newPos
                if (newScore >= 21)
                    wins[player] += universes * numberOfUniverses
                else {
                    positions[player] = newPos
                    scores[player] = newScore
                    game((player + 1) % 2, universes * numberOfUniverses, positions, scores)
                }
            }
            positions[player] = pos
            scores[player] = score
        }

        game(0, 1, start.toIntArray())

        return wins.maxOrNull()!!
    }

}

fun main() {
    solve<Day21>("""
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent(), 739785, 444356092776315)
}