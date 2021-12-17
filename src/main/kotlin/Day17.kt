import utils.*

class Day17 : Day(17, 2021) {

    val p = mappedInput { it.extractAllIntegers() }.first()
    val targetX = p[0]..p[1]
    val targetY = p[2]..p[3]

    fun Int.drag() = when (this) {
        0 -> 0
        in 1..Int.MAX_VALUE -> this - 1
        else -> this + 1
    }

    override fun part1(): Any {
        return 0

        fun maxHeight(vi: Point): Triple<Int?, Boolean, Boolean> {
            var p = origin
            var v = vi
            var max: Int = 0
            while (p.x <= targetX.last && p.y >= targetY.first) {
                //println(p)
                if (p.x in targetX && p.y in targetY) {
                    return Triple(max, true, true)
                }
                p += v
                max = maxOf(max, p.y)
                v = (v.x.drag() to v.y - 1)
            }
            return Triple(null,
                p.y < targetY.first,
                (v.x == 0 && p.y + v.y < targetY.first) || p.x > targetX.last)
        }

//        println(maxHeight(7 to 2))
//        println(maxHeight(6 to 3))
//        println(maxHeight(9 to 0))
//        println(maxHeight(17 to -4))
        //  println(maxHeight(7 to 13000))
        //  return 0


        var max = 0
        for (vx in targetX.last downTo 1) {
            //for (vy in targetY.first..Int.MAX_VALUE) {
            for (vy in -10000..10000) {
                //print("$vx $vy - ")
                val (m, tooLow, tooHigh) = maxHeight(vx to vy)
//                if (m == null) {
//                    println("tooLow: $tooLow tooHigh: $tooHigh")
//                    if (tooHigh) break
//                    if (tooLow) continue
//                }
                if (m != null && m > max) max = m
                //println(" - $m - $max")
            }
        }


        return max
    }

    override fun part2(): Any {


        fun maxHeight(vi: Point): Triple<Int?, Boolean, Boolean> {
            var p = origin
            var v = vi
            var max: Int = 0
            while (p.x <= targetX.last && p.y >= targetY.first) {
                //println(p)
                if (p.x in targetX && p.y in targetY) {
                    return Triple(max, true, true)
                }
                p += v
                max = maxOf(max, p.y)
                v = (v.x.drag() to v.y - 1)
            }
            return Triple(null,
                p.y < targetY.first,
                (v.x == 0 && p.y + v.y < targetY.first) || p.x > targetX.last)
        }

//        println(maxHeight(7 to 2))
//        println(maxHeight(6 to 3))
//        println(maxHeight(9 to 0))
//        println(maxHeight(17 to -4))
        //  println(maxHeight(7 to 13000))
        //  return 0


        var max = 0
        var count = 0
        for (vx in targetX.last downTo 1) {
            //for (vy in targetY.first..Int.MAX_VALUE) {
            for (vy in -10000..10000) {
                //print("$vx $vy - ")
                val (m, tooLow, tooHigh) = maxHeight(vx to vy)
//                if (m == null) {
//                    println("tooLow: $tooLow tooHigh: $tooHigh")
//                    if (tooHigh) break
//                    if (tooLow) continue
//                }
                if (m != null) count++
                if (m != null && m > max) max = m
                //println(" - $m - $max")
            }
        }


        return count
    }

}

fun main() {
    solve<Day17>("""
        target area: x=20..30, y=-10..-5
    """.trimIndent(), null, 112)
}