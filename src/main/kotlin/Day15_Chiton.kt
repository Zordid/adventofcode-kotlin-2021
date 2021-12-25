import com.github.ajalt.mordant.rendering.TextColors
import utils.*

class Day15 : Day(15, 2021, "Chiton") {

    val risks: Grid<Int> = mappedInput { it.toList().map { it.digitToInt() } }
    val area = risks.area

    val graphSmall = object : Graph<Point> {
        override fun neighborsOf(node: Point): Collection<Point> = node.directNeighbors(area)
        override fun cost(from: Point, to: Point): Int = risks[to]
        override fun costEstimation(from: Point, to: Point): Int = from manhattanDistanceTo to
    }

    val graphBig = object : Graph<Point> {
        private val bigArea = area.scale(5)

        override fun neighborsOf(node: Point): Collection<Point> = node.directNeighbors(bigArea)
        override fun cost(from: Point, to: Point): Int {
            val blockOffset = to / area.width
            val baseRisk = risks[to % area.width]
            return ((baseRisk - 1 + blockOffset.manhattanDistance) % 9) + 1
        }

        override fun costEstimation(from: Point, to: Point): Int = from manhattanDistanceTo to
    }

    override fun part1(): Int = findMinRisk(graphSmall, area.lowerRight)

    override fun part2(): Int = findMinRisk(graphBig, area.scale(5).lowerRight)

    private fun findMinRisk(graph: Graph<Point>, dest: Point, show: Boolean = false) =
        graph.aStarSearch(origin, dest).also {
            if (show) {
                val color = TextColors.brightWhite on TextColors.red
                val stack = it.buildStack().toSet()
                for (y in 0..dest.y) {
                    println((0..dest.x).joinToString("") { x ->
                        val p = x to y
                        val risk = graph.cost(origin, p)
                        if (p in stack)
                            color(risk.toString())
                        else
                            risk.toString()
                    })
                }
            }
        }.distanceToStart!!
}

fun main() {
    solve<Day15>("""
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent(), 40, 315)
}