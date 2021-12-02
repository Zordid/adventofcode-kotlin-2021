class Day01 : Day(1, 2021, "Sonar Sweep") {

    private val depthMeasurements = inputAsLongs

    override fun part1(): Int =
        depthMeasurements.countIncreases()

    override fun part2(): Int =
        depthMeasurements.windowed(3) { it.sum() }.countIncreases()

    private fun List<Long>.countIncreases() =
        asSequence().zipWithNext().count { (a, b) -> b > a }

}

fun main() {
    solve<Day01>()
}