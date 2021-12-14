class Day14 : Day(14, 2021) {

    val template = input.first().show("Template")
    val insertions = chunkedInput()[1].map {
        val (a, b) = it.split(" -> ")
        a to b
    }.toMap()

    fun String.process(): String {
        val w = this.windowed(2)
        return w.joinToString(prefix = "${this[0]}", separator = "") { pair ->
            insertions[pair]?.let { "$it${pair[1]}" } ?: pair
        }
    }

    override fun part1(): Any {
        val r = (1..10).fold(template) { acc, _ -> acc.process() }
        val stats = r.groupingBy { it }.eachCount()
        val least = stats.minByOrNull { (_, c) -> c }!!.value
        val most = stats.maxByOrNull { (_, c) -> c }!!.value
        return most - least
    }

    override fun part2(): Any {
        val statsCache = mutableMapOf<String, Map<Char, Int>>()
        val r20 = (1..20).fold(template) { acc, _ -> acc.process() }
        val stats = mutableMapOf(template.first() to 1L)
        r20.windowed(2).forEach { pair ->
            val ms = statsCache.getOrPut(pair) {
                println("Processing $pair...")
                val r = (1..20).fold(pair) { acc, _ -> acc.process() }
                r.drop(1).groupingBy { it }.eachCount()
            }
            ms.forEach { (k, c) ->
                stats[k] = (stats[k] ?: 0) + c
            }
        }
        val least = stats.minByOrNull { (_, c) -> c }!!.value
        val most = stats.maxByOrNull { (_, c) -> c }!!.value
        return most - least
    }

    /*

NB - NBB - NBBNB -

     */

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