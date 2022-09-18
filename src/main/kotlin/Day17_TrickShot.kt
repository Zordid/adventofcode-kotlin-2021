import utils.*
import kotlin.math.sign

class Day17 : Day(17, 2021, "Trick Shot") {

    private val ints = mappedInput { it.extractAllIntegers() }.first()
    val targetX = (ints[0]..ints[1]).also { require(!it.isEmpty()) }
    val targetY = (ints[2]..ints[3]).also { require(!it.isEmpty()) }

    private fun maxHeight(vi: Point): Int? {
        var p = origin
        var v = vi
        var max = 0
        while (p.x <= targetX.last && p.y >= targetY.first) {
            if (p.x in targetX && p.y in targetY)
                return max
            p += v
            max = maxOf(max, p.y)
            v = (v.x - v.x.sign to v.y - 1)
        }
        return null
    }

    override fun part1() = possibleVelocities.maxByOrNull { it.value }!!.value

    override fun part2() = possibleVelocities.count()

    val possibleVelocities: Map<Point, Int> by lazy {
        (targetX.last downTo 1).flatMap { vx ->
            (targetY.first..-targetY.last * 2).map { vy ->
                vx to vy
            }
        }.mapNotNull { v -> maxHeight(v)?.let { v to it } }
            .toMap()
    }

}

fun main() {
    solve<Day17>(
        """
        target area: x=20..30, y=-10..-5
    """.trimIndent(), 45, 112
    )
}