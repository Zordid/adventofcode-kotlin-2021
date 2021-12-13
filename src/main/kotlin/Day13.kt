import utils.*

class Day13 : Day(13, 2021) {

    val p = chunkedInput().let {
        it[0].map { it.extractAllIntegers().let { (x, y) -> Point(x, y) } } to it[1].map {
            val (xy, v) = it.split("=")
            xy.last() to v.toInt()
        }
    }
    val paper = p.first.show("Paper")
    val fold = p.second.show("Folds")

    fun List<Point>.foldY(yf: Int): List<Point> {
        return map { (x, y) ->
            if (y > yf)
                x to yf - (y - yf)
            else
                x to y
        }.distinct()
    }

    fun List<Point>.foldX(xf: Int): List<Point> {
        return map { (x, y) ->
            if (x > xf)
                xf - (x - xf) to y
            else
                x to y
        }.distinct()
    }

    override fun part1(): Any {
        var p = paper
        for ((xy, v) in fold.take(1)) {
            p = when (xy) {
                'x' -> p.foldX(v)
                'y' -> p.foldY(v)
                else -> error("")
            }
        }
        return p.size
    }

    override fun part2(): Any {
        var p = paper
        for ((xy, v) in fold) {
            p = when (xy) {
                'x' -> p.foldX(v)
                'y' -> p.foldY(v)
                else -> error("")
            }
        }
        p.plot()
        return Unit
    }
}

fun main() {
    solve<Day13>("""
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0

        fold along y=7
        fold along x=5
    """.trimIndent(), 17, null)
}