class Day03 : Day(3, 2021) {

    val x = input

    override fun part1(): Any? {
        val l = x.size
        val b = x.first().length
        var r = ""
        for (i in 0 until b) {
            val zeros = x.map { it[i] }.count { it == '0' }
            val ones = x.map { it[i] }.count { it == '1' }
            if (zeros > ones) r += '0' else r += '1'
        }

        val gamma = r.toInt(2)
        println(gamma)
        r = ""
        for (i in 0 until b) {
            val zeros = x.map { it[i] }.count { it == '0' }
            val ones = x.map { it[i] }.count { it == '1' }
            if (zeros < ones) r += '0' else r += '1'
        }
        val epsilon = r.toInt(2)
        println(epsilon)
        return gamma * epsilon
    }

    override fun part2(): Any? {
        var oxy = x
        val b = x.first().length

        for (i in 0 until b) {
            val zeros = oxy.map { it[i] }.count { it == '0' }
            val ones = oxy.map { it[i] }.count { it == '1' }
            if (ones >= zeros) {
                oxy = oxy.filter { it[i] == '1' }
            } else
                oxy = oxy.filter { it[i] == '0' }
            if (oxy.size == 1) break
        }
        val o = oxy.single().toInt(2)
        println(o)

        var co2 = x
        for (i in 0 until b) {
            val zeros = co2.map { it[i] }.count { it == '0' }
            val ones = co2.map { it[i] }.count { it == '1' }
            if (ones < zeros) {
                co2 = co2.filter { it[i] == '1' }
            } else
                co2 = co2.filter { it[i] == '0' }
            if (co2.size == 1) break
        }
        val c = co2.single().toInt(2)
        println(c)

        return c * o
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
