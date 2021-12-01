class Day01 : Day(1, 2021, "Sonar Sweep") {

    private val depthMeasurements = inputAsLongs

    override fun part1(): Int =
        depthMeasurements.countIncreases()

    override fun part2(): Int =
        depthMeasurements.windowed(3, 1).map { it.sum() }.countIncreases()

    private fun List<Long>.countIncreases() =
        windowed(2, 1).count { (a, b) -> b > a }

}

fun main() {
    Day01().solve()
}