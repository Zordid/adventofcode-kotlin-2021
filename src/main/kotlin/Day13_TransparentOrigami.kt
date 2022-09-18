import utils.Point
import utils.plot

class Day13 : Day(13, 2021, "Transparent Origami") {

    data class FoldOperation(val xy: Char, val line: Int)

    val paper = inputChunks[0].map {
        it.extractAllIntegers().let { (x, y) -> Point(x, y) }
    }.show("Paper")

    val folds = inputChunks[1].map {
        val (xy, v) = it.split("=")
        FoldOperation(xy.last(), v.toInt())
    }.show("Folds")

    fun List<Point>.fold(fold: FoldOperation): List<Point> = map { (x, y) ->
        when {
            fold.xy == 'y' && y > fold.line -> x to 2 * fold.line - y
            fold.xy == 'x' && x > fold.line -> 2 * fold.line - x to y
            else -> x to y
        }
    }.distinct()

    override fun part1() = paper.fold(folds.first()).size

    override fun part2() =
        folds.fold(paper) { acc, fold ->
            acc.fold(fold)
        }.plot()

}

fun main() {
    solve<Day13>(
        """
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
    """.trimIndent(), 17
    )
}