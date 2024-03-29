import utils.Direction8
import utils.Point
import utils.plus

class Day05 : Day(5, 2021, "Hydrothermal Venture") {

    data class Line(val p1: Point, val p2: Point) : List<Point> by listOf(p1, p2) {
        private val orientation = Direction8.ofVector(p1, p2)
        val isHorizontalOrVertical =
            orientation in listOf(Direction8.NORTH, Direction8.SOUTH, Direction8.EAST, Direction8.WEST)
        val allPoints: List<Point> = buildList {
            var p = p1
            while (p != p2) add(p).also { p += orientation!! }
            add(p)
        }
    }

    val lines: List<Line> =
        mappedInput { it.extractAllIntegers().let { (x1, y1, x2, y2) -> Line((x1 to y1), (x2 to y2)) } }

    override fun part1(): Int {
        val hv = lines.filter { it.isHorizontalOrVertical }
//        val a = Array(1000) { IntArray(1000) { 0 } }
//        var c = 0
//        hv.forEach {
//            it.allPoints.forEach { (x, y) ->
//                val was = a[y][x]
//                when (was) {
//                    0 -> a[y][x] = 1
//                    1 -> {
//                        a[y][x] = 2
//                        c++
//                    }
//                }
//            }
//        }
//        return c
        return hv.flatMap { it.allPoints }.groupingBy { it }.eachCount().count { it.value > 1 }
    }

    override fun part2(): Int {
//        val a = Array(1000) { IntArray(1000) { 0 } }
//        var c = 0
//        lines.forEach {
//            it.allPoints.forEach { (x, y) ->
//                val was = a[y][x]
//                when (was) {
//                    0 -> a[y][x] = 1
//                    1 -> {
//                        a[y][x] = 2
//                        c++
//                    }
//                }
//            }
//        }
//        return c
        return lines.flatMap { it.allPoints }.groupingBy { it }.eachCount().count { it.value > 1 }
    }

}

fun main() {
    solve<Day05>(
        """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent(), 5, 12
    )
}