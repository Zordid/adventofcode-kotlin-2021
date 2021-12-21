class Day21 : Day(21, 2021) {

    val start = mappedInput { it.extractAllIntegers().last() }

    override fun part1(): Any {
        val scores = mutableListOf(0, 0)
        val pos = start.toMutableList()

        var diceNext = 1
        var rolled = 0

        fun next() = diceNext.also {
            diceNext = (((diceNext + 1) - 1) % 1000) + 1
            rolled++
        }

        while (true) {
            for (p in 0..1) {
                val rolls = next() + next() + next()
                pos[p] = (pos[p] - 1 + rolls) % 10 + 1
                scores[p] += pos[p]
                if (scores[p] >= 1000) {
                    return rolled * scores[(p + 1) % 2]
                }
            }
        }

        return super.part1()
    }

    override fun part2(): Any {
        println(3 * 3 * 3)
        val c = (1..3).flatMap { a ->
            (1..3).flatMap { b ->
                (1..3).map { c ->
                    listOf(a, b, c)
                }
            }
        }
        c.forEach(::println)

        val universes = c.groupingBy { it.sum() }.eachCount()
        println(universes)

        val wins = mutableListOf(0L, 0L)

        fun game(player: Int, u: Long, pos: List<Int>, scores: List<Int>) {
            val p = pos[player]
            val s = scores[player]
            for ((total, times) in universes) {
                val newPos = (p + total - 1) % 10 + 1
                val newScore = s + newPos
                if (newScore >= 21)
                    wins[player] = wins[player] + u * times
                else {
                    val copyScore = scores.toMutableList()
                    copyScore[player]=newScore
                    val copyPos = pos.toMutableList()
                    copyPos[player]=newPos
                    game((player + 1) % 2, u * times, copyPos, copyScore)
                }
            }
        }

        game(0, 1, start, listOf(0,0))


        return wins.maxOrNull()!!
    }

}

fun main() {
    solve<Day21>("""
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent(), 739785, 444356092776315)
}