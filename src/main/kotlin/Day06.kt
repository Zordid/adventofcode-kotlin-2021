import java.math.BigInteger

class Day06 : Day(6, 2021, "Lanternfish") {

    private val population = mappedInput { it.extractAllIntegers() }.first()

    private fun getInitialAges(): Array<BigInteger> = Array(9) { age ->
        population.count { it == age }.toBigInteger()
    }

    private fun Array<BigInteger>.simulateCycles(n: Int): Array<BigInteger> {
        var ages = this
        repeat(n) {
            ages = ages.shiftLeft()
            ages[6] = ages[6] + ages[lastIndex]
        }
        return ages
    }

    override fun part1(): BigInteger = getInitialAges().simulateCycles(80).sum()

    override fun part2(): BigInteger = getInitialAges().simulateCycles(256).sum()

    companion object {
        private fun Array<BigInteger>.sum() = reduce(BigInteger::plus)

        private inline fun <reified T> Array<T>.shiftLeft() = Array(size) { this[(it + 1) % size] }
    }
}

fun main() {
    solve<Day06>("""3,4,3,1,2""", 5934, 26984457539)
}