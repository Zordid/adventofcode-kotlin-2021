class Day10 : Day(10, 2021) {

    override fun part1(): Any {
        return input.sumOf { line -> line.isCorrupted() }
    }

    fun String.isCorrupted(): Int {
        val opened = mutableListOf<Char>()

        var expected: Char? = null
        var found: Char? = null
        for (i in this) {
            if (i.isOpening())
                opened += i
            else {
                expected = pairs[opened.lastOrNull()]
                if (i == expected)
                    opened.removeLast()
                else {
                    found = i
                    break
                }
            }
        }

        return illegal[found.toString()] ?: 0
    }

    override fun part2(): Any {
        return input.filter { it.isCorrupted() == 0 }.map { line ->
            val opened = mutableListOf<Char>()

            var expected: Char? = null
            for (i in line) {
                if (i.isOpening())
                    opened += i
                else {
                    expected = pairs[opened.lastOrNull()]
                    if (i == expected)
                        opened.removeLast()
                    else {
                        error("Nope")
                    }
                }
            }

            var score = 0

            opened.reversed().fold(0L) { acc, c ->
                acc * 5 + when (c) {
                    '(' -> 1
                    '[' -> 2
                    '{' -> 3
                    '<' -> 4
                    else -> error(c.toString())
                }
            }
        }.sorted().let {
            it[(it.size-1) / 2]
        }
    }

}

fun Char.isOpening() = this in pairs

val pairs = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>'
)

val illegal = mapOf(
    ")" to 3,
    "]" to 57,
    "}" to 1197,
    ">" to 25137
)

fun main() {
    solve<Day10>("""
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent(), 26397, 288957)
}