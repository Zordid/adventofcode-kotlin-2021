class Day10 : Day(10, 2021, "Syntax Scoring") {

    override fun part1() = input.sumOf {
        val (illegal) = it.firstIllegalOrStack()
        illegalScore[illegal] ?: 0
    }

    override fun part2() = input
        .mapNotNull { line->
            line.firstIllegalOrStack().second
        }.map { stack ->
            stack.reversed().fold(0L) { score, c -> score * 5 + completionScore[c]!! }
        }.sorted().let {
            it[(it.size - 1) / 2]
        }

    private fun String.firstIllegalOrStack(): Pair<Char?, List<Char>?> {
        val opened = mutableListOf<Char>()
        for (i in this) {
            when {
                i.isOpening() -> opened += i
                i == closing[opened.lastOrNull()] -> opened.removeLast()
                else -> return i to null
            }
        }
        return null to opened
    }

    companion object {
        fun Char.isOpening() = this in closing

        val closing = mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>'
        )

        val illegalScore = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )

        val completionScore = mapOf(
            '(' to 1,
            '[' to 2,
            '{' to 3,
            '<' to 4
        )
    }

}

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