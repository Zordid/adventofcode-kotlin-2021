import utils.minMaxOrNull

class Day14 : Day(14, 2021, "Extended Polymerization") {

    private val template = chunkedInput()[0].first()
    private val rules = chunkedInput()[1].associate {
        val (from, to) = it.split(" -> ")
        (from[0] to from[1]) to to.first()
    }

    override fun part1(): Long = template.process(10).mostMinusLeast()
    override fun part2(): Long = template.process(40).mostMinusLeast()

    private fun String.process(steps: Int): Map<Char, Long> =
        windowed(2)
            .fold(mapOf(first() to 1L)) { acc, p ->
                acc + (p[0] to p[1]).stats(steps)
            }

    private fun Map<Char, Long>.mostMinusLeast(): Long =
        values.minMaxOrNull()!!.let { it.second - it.first }

    private val globalCache = mutableMapOf<Triple<Char, Char, Int>, Map<Char, Long>>()

    private fun Pair<Char, Char>.stats(steps: Int): Map<Char, Long> =
        globalCache.getOrPut(Triple(first, second, steps)) {
            val insertion = rules[this]
            when {
                steps == 0 || insertion == null ->
                    mapOf(second to 1L)
                else ->
                    ((first to insertion).stats(steps - 1) +
                            (insertion to second).stats(steps - 1))
            }
        }

    companion object {
        private operator fun Map<Char, Long>.plus(other: Map<Char, Long>): Map<Char, Long> =
            buildMap(size) {
                putAll(this@plus)
                other.forEach { (k, v) ->
                    put(k, getOrDefault(k, 0) + v)
                }
            }
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