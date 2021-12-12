class Day12 : Day(12, 2021, "Passage Pathing") {

    private val connections: Map<String, Set<String>> = mappedInput { it.split("-") }
        .let {
            buildMap<String, MutableSet<String>> {
                it.forEach { (s, e) ->
                    if (s != END && e != START)
                        getOrPut(s) { mutableSetOf() } += e
                    if (e != END && s != START)
                        getOrPut(e) { mutableSetOf() } += s
                }
            }
        }

    override fun part1(): Int {
        var count = 0
        visitCave(START) { count++ }
        return count
    }

    override fun part2(): Int {
        var count = 0
        visitCave(START, 1) { count++ }
        return count
    }

    private fun visitCave(
        cave: String,
        boredomTolerance: Int = 0,
        visited: CavePath = emptyList(),
        onEndFound: (CavePath) -> Unit,
    ) {
        if (cave == END) return onEndFound(visited + cave)

        val howBoringIsThisCave = if (cave.isSmall && cave in visited) 1 else 0
        if (boredomTolerance - howBoringIsThisCave < 0) return

        connections[cave]?.forEach {
            visitCave(it, boredomTolerance - howBoringIsThisCave, visited + cave, onEndFound)
        }
    }

    companion object {
        private const val START = "start"
        private const val END = "end"

        private val String.isSmall: Boolean get() = first().isLowerCase()
    }
}

typealias CavePath = List<String>

fun main() {
    solve<Day12>("""
        fs-end
        he-DX
        fs-he
        start-DX
        pj-DX
        end-zg
        zg-sl
        zg-pj
        pj-he
        RW-he
        fs-DX
        pj-RW
        zg-RW
        start-pj
        he-WI
        zg-he
        pj-fs
        start-RW
    """.trimIndent(), 226, 3509)
}