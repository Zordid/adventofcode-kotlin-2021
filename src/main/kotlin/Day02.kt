import Day02.Command.*

class Day02 : Day(2, 2021, "Dive!") {

    enum class Command { FORWARD, UP, DOWN }

    private val course = parsedInput { valueOf(cols.first().uppercase()) to ints.first() }

    override fun part1(): Int =
        course.fold(0 to 0) { (pos, depth), (cmd, x) ->
            when (cmd) {
                FORWARD -> pos + x to depth
                UP -> pos to depth - x
                DOWN -> pos to depth + x
            }
        }.let { (pos, depth) -> pos * depth }

    override fun part2(): Int =
        course.fold(Triple(0, 0, 0)) { (pos, depth, aim), (cmd, x) ->
            when (cmd) {
                FORWARD -> Triple(pos + x, depth + aim * x, aim)
                UP -> Triple(pos, depth, aim - x)
                DOWN -> Triple(pos, depth, aim + x)
            }
        }.let { (pos, depth) -> pos * depth }

}

fun main() {
    solve<Day02>("""
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent(), 150, 900)
}