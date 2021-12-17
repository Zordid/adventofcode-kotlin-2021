import utils.*
import kotlin.math.sign

class Day17 : Day(17, 2021, "Trick Shot") {

    private val ints = mappedInput { it.extractAllIntegers() }.first()
    private val targetX = ints[0]..ints[1]
    private val targetY = ints[2]..ints[3]

    private fun maxHeight(vi: Point): Int? {
        var p = origin
        var v = vi
        var max = 0
        while (p.x <= targetX.last && p.y >= targetY.first) {
            if (p.x in targetX && p.y in targetY)
                return max
            if (v.y > -targetY.last * 2)
                return null
            p += v
            max = maxOf(max, p.y)
            v = (v.x - v.x.sign to v.y - 1)
        }
        return null
    }

    override fun part1(): Any {
        var max = 0
        for (vx in targetX.last downTo 1) {
            //for (vy in targetY.first..Int.MAX_VALUE) {
            for (vy in -10000..10000) {
                val m = maxHeight(vx to vy)
                if (m != null && m > max) max = m
            }
        }
        return max
    }

    override fun part2(): Any {
        var count = 0
        for (vx in targetX.last downTo 1) {
            //for (vy in targetY.first..Int.MAX_VALUE) {
            for (vy in -10000..10000) {
                val m = maxHeight(vx to vy)
                if (m != null) count++
            }
        }
        return count
    }

}

fun main() {
    solve<Day17>("""
        target area: x=20..30, y=-10..-5
    """.trimIndent(), 45, 112)
}