import utils.*

class Day11 : Day(11, 2021) {

    val levels: Grid = mappedInput { it.toList().map { it.digitToInt() } }
    val area = levels.area()

    fun Grid.step(): Pair<Grid, Int> {
        val l = this.map { it.map { it + 1 }.toMutableList() }

        val flashed = mutableSetOf<Point>()

        fun flash(p: Point) {
            if (p in flashed) return
            flashed += p
            p.surroundingNeighbors().forEach { n ->
                if (n in area && ++l[n.y][n.x] > 9) flash(n)
            }
        }

        area.forEach { p ->
            if (l[p]!! > 9) flash(p)
        }
        flashed.forEach { p ->
            l[p.y][p.x] = 0
        }
        return l to flashed.size
    }

    override fun part1(): Any {
        return (1..100).fold(levels to 0) { (l, acc), _ ->
//            l.forEach { println(it.joinToString("")) }
//            println("Total: $acc")
//            println()
            l.step().let { it.first to it.second + acc }
        }.second
    }

    override fun part2(): Any {
        return (1..Int.MAX_VALUE).asSequence()
            .onEach { print(it) }
            .runningFold(levels to 0) { (l, _), _ -> l.step() }
            .onEach { println(": ${it.second}") }
            .takeWhile { (_,f)-> f < area.size }
            .count()
    }

}

typealias Grid = List<List<Int>>

fun main() {
    solve<Day11>("""
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent(), 1656, 195)
}