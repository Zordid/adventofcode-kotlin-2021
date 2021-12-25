import utils.*

class Day25 : Day(25, 2021, "Sea Cucumber") {

    val seaFloor: Grid<Char> = mappedInput { it.toList() }

    data class State(val area: Area, val east: Set<Point>, val south: Set<Point>)

    fun State.step(): State {
        val newEast = east.map { p ->
            val np = p.right().wrappedAroundIn(area)
            if (np !in east && np !in south) np else p
        }.toSet()
        val newSouth = south.map { p ->
            val np = p.down().wrappedAroundIn(area)
            if (np !in south && np !in newEast) np else p
        }.toSet()

        return copy(east = newEast, south = newSouth)
    }

    private fun Point.wrappedAroundIn(area: Area): Point =
        Point(x % area.width, y % area.height)

    override fun part1(): Any {
        var state = State(
            seaFloor.area,
            seaFloor.searchIndices { it == '>' }.toSet(),
            seaFloor.searchIndices { it == 'v' }.toSet(),
        )
        var step = 0
        while (true) {
            step++
            val ns = state.step()
            if (ns == state)
                return step
            state = ns
        }
    }

    override fun part2() = "Merry Christmas!"

}

fun main() {
    solve<Day25>("""
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent(), 58)
}