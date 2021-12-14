import utils.minMaxOrNull

class Day14 : Day(14, 2021, "Extended Polymerization") {

    private val template = chunkedInput()[0].single()
    private val rules = chunkedInput()[1].associate {
        val (from, to) = it.split(" -> ")
        (from[0] to from[1]) to to.first()
    }

    override fun part1(): Long = template.process(10).mostMinusLeast()
    override fun part2(): Long = template.process(40).mostMinusLeast()

    private fun String.process(steps: Int): LongArray =
        windowed(2)
            .fold(first().stats()) { acc, p ->
                acc + (p[0] to p[1]).stats(steps)
            }

    private val globalCache =
        mutableMapOf<Triple<Char, Char, Int>, LongArray>()

    private fun Pair<Char, Char>.stats(steps: Int): LongArray =
        globalCache.getOrPut(Triple(first, second, steps)) {
            val insertion = rules[this]
            when {
                steps == 0 || insertion == null -> second.stats()
                else ->
                    ((first to insertion).stats(steps - 1) +
                            (insertion to second).stats(steps - 1))
            }
        }

    companion object {
        private const val SIZE = 'Z' - 'A'

        private operator fun LongArray.plus(other: LongArray) =
            LongArray(SIZE) { this[it] + other[it] }

        private fun Char.stats() =
            LongArray(SIZE) { if (it == this.code - 'A'.code) 1L else 0L }

        private fun LongArray.mostMinusLeast() =
            asIterable().filter { it != 0L }.minMaxOrNull()!!.let { it.second - it.first }
    }

}

fun main() {
    solve<Day14>("""
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent(), 1588, 2188189693529)
}