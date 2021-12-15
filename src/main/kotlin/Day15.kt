import utils.*

class Day15 : Day(15, 2021) {

    val risks = mappedInput { it.toList().map { it.digitToInt() } }
    val area = risks.area()
    val dest = (area.width - 1) to (area.height - 1)

    override fun part1(): Any {

        val totalRisk = risks.mapValuesIndexed { p, v -> v + (p manhattanDistanceTo origin) * 9 }.toMutableGrid()
        totalRisk[origin] = 0

        fun findPath(pos: Point, risk: Int = 0) {
            if (totalRisk[pos] < risk) return
            totalRisk[pos] = risk

            val neighbors = pos.directNeighbors().filter { it in area }

            neighbors.forEach {
                if (totalRisk[it] > risk + risks[it])
                    findPath(it, risk + risks[it])
            }
        }
        findPath(origin)

        return totalRisk[dest]
    }

    override fun part2(): Any {

        fun Int.fix(): Int =
            if (this > 9) (this - 9).fix() else this

        val big = mutableListOf<List<Int>>()
        repeat(5) { outerCycle ->
            big += risks.map { o ->
                (0 until o.size * 5).map { idx ->
                    val cycle = idx / o.size
                    (o[idx % o.size] + cycle + outerCycle).fix()
                }
            }.toMutableList()
        }

        val totalRisk = big.mapValuesIndexed { p, v -> v + (p manhattanDistanceTo origin) * 9 }.toMutableGrid()
        totalRisk[origin] = 0

        fun findPath(pos: Point, risk: Int = 0) {
            if (totalRisk[pos] < risk) return
            totalRisk[pos] = risk

            val neighbors = pos.directNeighbors().filter { it in big.area() }

            neighbors.forEach {
                if (totalRisk[it] > risk + big[it])
                    findPath(it, risk + big[it])
            }
        }
        findPath(origin)

        return totalRisk[big.area().let { it.width-1 to it.height-1 }]
    }
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