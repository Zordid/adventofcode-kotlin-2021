import utils.*

class Day09 : Day(9, 2021) {

    private val heights = mappedInput { it.toList().map { it.digitToInt() } }
    private val bounds = heights.area()

    override fun part1() = bounds.allPoints().sumOf { p ->
        if (isLow(p)) heights[p]!! + 1 else 0
    }

    override fun part2() =
        bounds.allPoints().filter { isLow(it) }
            .map { floodFill(it).size }
            .sortedDescending().take(3)
            .reduce(Int::times)

    private fun isLow(p: Point) =
        p.directNeighbors().filter { it in bounds }.all { n ->
            heights[n]!! > heights[p]!!
        }

    private fun floodFill(p: Point, basin: MutableSet<Point> = mutableSetOf()): Set<Point> {
        if (p !in bounds || heights[p] == 9 || p in basin) return basin
        basin += p
        floodFill(p + Direction4.UP, basin)
        floodFill(p + Direction4.DOWN, basin)
        listOf(Direction4.LEFT, Direction4.RIGHT).forEach { d ->
            var l = p + d
            while (l in bounds && heights[l] != 9) {
                basin += l
                floodFill(l + Direction4.UP, basin)
                floodFill(l + Direction4.DOWN, basin)
                l += d
            }
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