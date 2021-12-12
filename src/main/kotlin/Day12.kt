class Day12 : Day(12, 2021) {

    val con = mappedInput {
        val (s, e) = it.split("-")
        s to e
    }.let {
        buildMap {
            it.forEach { (s, e) ->
                getOrPut(s) { mutableSetOf<String>() } += e
                getOrPut(e) { mutableSetOf<String>() } += s
            }
        }
    }

    override fun part1(): Any {
        val paths = mutableListOf<List<String>>()

        fun visit(node: String, visited: List<String>) {
            if (node == "end") {
                paths += (visited + node)
                return
            }
            if ((node == "start" || node.all { it.isLowerCase() }) && node in visited) return

            val dests = con[node]
            dests?.forEach {
                visit(it, visited + node)
            }
        }

        visit("start", emptyList())
        return paths.count()
    }

    override fun part2(): Any {
        val paths = mutableListOf<List<String>>()

        fun visit(node: String, visited: List<String>, hadTwice: String? = null) {
            if (node == "end") {
                paths += (visited + node)
                return
            }
            if (node == "start" && visited.isNotEmpty()) return

            var hT = hadTwice
            val smallCave = node.all { it.isLowerCase() }
            if (smallCave && node in visited) {
                if (hadTwice != null)
                    return
                hT = node
            }

            val dests = con[node]
            val newPath = visited + node
            dests?.forEach {
                visit(it, newPath, hT)
            }
        }

        visit("start", emptyList())
        return paths.count()
    }

}

fun main() {
    solve<Day12>("""
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent(), 10, 36)
}