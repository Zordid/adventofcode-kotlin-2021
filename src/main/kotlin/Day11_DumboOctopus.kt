import utils.*

class Day11 : Day(11, 2021, "Dumbo Octopus") {

    val levels: EnergyGrid = mappedInput { it.toList().map { it.digitToInt() } }
    private val area = levels.area

    override fun part1() =
        (1..100).fold(levels to 0) { (l, flashedAcc), _ ->
            l.step().let { (l, f) -> l to f.size + flashedAcc }
        }.second

    override fun part2() =
        (1..Int.MAX_VALUE).asSequence()
            .runningFold(levels to emptySet<Point>()) { (l, _), _ -> l.step() }
            .takeWhile { (_, flashed) -> flashed.size < area.size }
            .count()

    fun EnergyGrid.step(): Pair<EnergyGrid, Set<Point>> {
        val nextLevels = mapValues { it + 1 }.toMutableGrid()
        val flashed = mutableSetOf<Point>()

        fun flash(p: Point) {
            if (flashed.add(p)) {
                p.surroundingNeighbors().forEach { n ->
                    if (n in area && ++nextLevels[n] > 9) flash(n)
                }
            }
        }

        forArea { p -> if (nextLevels[p] > 9) flash(p) }
        flashed.forEach { p -> nextLevels[p] = 0 }
        return nextLevels to flashed
    }

}

typealias EnergyGrid = Grid<Int>

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