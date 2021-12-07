import utils.minMaxOrNull
import kotlin.math.absoluteValue

class Day07 : Day(7, 2021, "The Treachery of Whales") {

    private val crabPositions = mappedInput { it.extractAllIntegers() }.first()
    private val positionsRange = crabPositions.minMaxOrNull()!!.let { it.first..it.second }

    private fun calcFuelCosts(destPosition: Int): Int =
        crabPositions.sumOf { (destPosition - it).absoluteValue }

    private fun calcFuelCostsPart2(destPosition: Int): Int =
        crabPositions.sumOf {
            val dist = (destPosition - it).absoluteValue
            (dist * (dist + 1)) / 2
        }

    override fun part1() = positionsRange.map { calcFuelCosts(it) }.minOf { it }

    override fun part2() = positionsRange.map { calcFuelCostsPart2(it) }.minOf { it }

}

fun main() {
    solve<Day07>("""16,1,2,0,4,2,7,1,2,14""", 37, 168)
}