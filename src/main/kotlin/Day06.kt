import java.math.BigInteger

class Day06 : Day(6, 2021) {

    val p = mappedInput { it.extractAllIntegers() }.first()

    override fun part1(): Any? {
        val lanterns = Array(9) { c ->
            p.count { it == c }.toBigInteger()
        }
        repeat(80) {
            println("$it: ${lanterns.joinToString()}")
            val zeros = lanterns.first()
            (0 until lanterns.lastIndex).forEach { lanterns[it] = lanterns[it + 1] }
            lanterns[6] = lanterns[6] + zeros
            lanterns[lanterns.lastIndex] = zeros
        }
        return lanterns.reduce(BigInteger::plus)
    }

    override fun part2(): Any? {
        val lanterns = Array(9) { c ->
            p.count { it == c }.toBigInteger()
        }
        repeat(256) {
            println("$it: ${lanterns.joinToString()}")
            val zeros = lanterns.first()
            (0 until lanterns.lastIndex).forEach { lanterns[it] = lanterns[it + 1] }
            lanterns[6] = lanterns[6] + zeros
            lanterns[lanterns.lastIndex] = zeros
        }
        return lanterns.reduce(BigInteger::plus)
    }

}

fun main() {
    solve<Day06>("""3,4,3,1,2""", 5934, 26984457539)
}