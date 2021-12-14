class Day06 : Day(6, 2021, "Lanternfish") {

    private val population = mappedInput { it.extractAllIntegers() }.first()

    private fun getInitialAges() = Array(9) { age ->
        population.count { it == age }.toLong()
    }

    override fun part1() = getInitialAges().simulateCycles(80).sum()

    override fun part2() = getInitialAges().simulateCycles(256).sum()

    companion object {
        private fun Array<Long>.simulateCycles(n: Int): Array<Long> {
            var ages = this
            repeat(n) {
                ages = ages.rotateLeft()
                ages[6] = ages[6] + ages[lastIndex]
            }
            return ages
        }
        private inline fun <reified T> Array<T>.rotateLeft() = Array(size) { this[(it + 1) % size] }
    }
}

fun main() {
    solve<Day06>("""3,4,3,1,2""", 5934, 26984457539)
}