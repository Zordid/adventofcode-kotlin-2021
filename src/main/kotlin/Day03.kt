class Day03 : Day(3, 2021, "Binary Diagnostic") {

    private val diag = input
    private val width = diag.first().indices

    private fun List<String>.countBitsAt(pos: Int): Pair<Int, Int> =
        with(map { it[pos] }) { count { it == '0' } to count { it == '1' } }

    override fun part1(): Int {
        val gamma = width.joinToString("") { pos ->
            val (zeros, ones) = diag.countBitsAt(pos)
            if (zeros > ones) "0" else "1"
        }.toInt(2)

        val epsilon = width.joinToString("") { pos ->
            val (zeros, ones) = diag.countBitsAt(pos)
            if (zeros < ones) "0" else "1"
        }.toInt(2)

        return gamma * epsilon
    }

    override fun part2(): Int {
        val oxygenRating = width.fold(diag) { remaining, pos ->
            if (remaining.size > 1) {
                val (zeros, ones) = remaining.countBitsAt(pos)
                remaining.filter { it[pos] == (if (ones >= zeros) '1' else '0') }
            } else
                remaining
        }.single().toInt(2)

        val co2Rating = width.fold(diag) { remaining, pos ->
            if (remaining.size > 1) {
                val (zeros, ones) = remaining.countBitsAt(pos)
                remaining.filter { it[pos] == (if (ones < zeros) '1' else '0') }
            } else
                remaining
        }.single().toInt(2)

        return oxygenRating * co2Rating
    }
}

fun main() {
    solve<Day03>("""
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010
    """.trimIndent(), 198, 230)
}
