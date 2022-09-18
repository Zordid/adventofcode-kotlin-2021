import utils.*

class Day20 : Day(20, 2021, "Trench Map") {

    private val enhancement = inputChunks[0].joinToString("").show("Enhancement")
    private val floor = inputChunks[1].map { it.toList() }.toMapGrid('.').withDefault { '.' }

    override fun part1() =
        floor.enhance().enhance().count { (_, v) -> v == '#' }

    override fun part2() =
        (0 until 50).fold(floor) { acc, _ -> acc.enhance() }
            .count { (_, v) -> v == '#' }


    private fun Map<Point, Char>.enhance(): Map<Point, Char> {
        val incoming = this
        val boundingArea = incoming.keys.boundingArea() ?: (origin to origin)
        val grownArea = boundingArea.grow(1)

        val background = incoming.getValue(Int.MAX_VALUE to Int.MAX_VALUE)
        val newBackground = if (background == '.') enhancement[0] else enhancement[511]

        return buildMap {
            grownArea.forEach { p ->
                val index =
                    (-1..1).flatMap { dy ->
                        (-1..1).map { dx ->
                            incoming.getValue(p + (dx to dy))
                        }
                    }.joinToString("") {
                        (if (it == '#') "1" else "0")
                    }.toInt(2)

                val newPixel = enhancement[index]
                if (newPixel != newBackground)
                    this[p] = newPixel
            }
        }.withDefault { newBackground }
    }

}

fun main() {
    solve<Day20>(
        """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
        #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
        .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
        .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
        .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
        ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
        ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent(), 35, 3351
    )
}