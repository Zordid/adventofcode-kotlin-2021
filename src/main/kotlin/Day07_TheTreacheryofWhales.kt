import utils.rangeOrNull
import kotlin.math.absoluteValue

class Day07 : Day(7, 2021, "The Treachery of Whales") {

    private val crabPositions = mappedInput { it.extractAllIntegers() }.first()
    private val positionsRange = crabPositions.rangeOrNull()!!

    private fun calcFuelCosts(destPosition: Int): Int =
        crabPositions.sumOf { (destPosition - it).absoluteValue }

    private fun calcFuelCostsPart2(destPosition: Int): Int =
        crabPositions.sumOf {
            val dist = (destPosition - it).absoluteValue
            (dist * (dist + 1)) / 2
        }

    override fun part1() = positionsRange.minOf { calcFuelCosts(it) }

    override fun part2() = positionsRange.minOf { calcFuelCostsPart2(it) }

}

fun main() {
    solve<Day07>("""16,1,2,0,4,2,7,1,2,14""", 37, 168)
}