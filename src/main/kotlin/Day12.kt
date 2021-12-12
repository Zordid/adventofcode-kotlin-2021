class Day12 : Day(12, 2021, "Passage Pathing") {

    private val connections: Map<String, Set<String>> = mappedInput { it.split("-") }
        .let {
            buildMap<String, MutableSet<String>> {
                it.forEach { (s, e) ->
                    if (s != "end" && e != "start")
                        getOrPut(s) { mutableSetOf() } += e
                    if (e != "end" && s != "start")
                        getOrPut(e) { mutableSetOf() } += s
                }
            }
        }

    override fun part1(): Any {

        fun visit(
            node: String,
            visited: List<String> = emptyList(),
            onEnd: (List<String>) -> Unit,
        ) {
            if (node == "end") return onEnd(visited + node)
            val isBoring = node.isSmallCave() && node in visited
            if (!isBoring)
                connections[node]?.forEach {
                    visit(it, visited + node, onEnd)
                }
        }

        var count = 0
        visit("start") { count++ }
        return count
    }

    override fun part2(): Any {

        fun visit(
            node: String,
            visited: List<String> = emptyList(),
            hadTwice: Boolean = false,
            onEnd: (List<String>) -> Unit,
        ) {
            if (node == "end") return onEnd(visited + node)

            val isBoring = node.isSmallCave() && node in visited
            if (!isBoring || !hadTwice)
                connections[node]?.forEach {
                    visit(it, visited + node, hadTwice || isBoring, onEnd)
                }
        }

        var count = 0
        visit("start") { count++ }
        return count
    }

    companion object {
        private fun String.isSmallCave() = first().isLowerCase()
    }
}

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