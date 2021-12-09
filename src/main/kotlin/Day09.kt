import utils.*

class Day09 : Day(9, 2021, "Smoke Basin") {

    val heights = mappedInput { it.toList().map { it.digitToInt() } }
    val bounds = heights.area()

    override fun part1() = bounds.allPoints().sumOf { p ->
        if (p.isLow()) heights[p]!! + 1 else 0
    }

    override fun part2() =
        bounds.allPoints().filter { it.isLow() }
            .map { floodFill(it).size }
            .sortedDescending().take(3)
            .reduce(Int::times)

    fun Point.isLow() =
        directNeighbors().filter { it in bounds }.all { n ->
            heights[n]!! > heights[this]!!
        }

    fun floodFill(p: Point, basin: MutableSet<Point> = mutableSetOf()): Set<Point> {
        if (p in bounds && heights[p] != 9 && p !in basin) {
            basin += p
            p.directNeighbors().forEach { floodFill(it, basin) }
        }
        return basin
    }

}

fun main() {
    solve<Day09>("""
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent(), 15, 1134)
}