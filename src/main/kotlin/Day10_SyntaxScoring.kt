class Day10 : Day(10, 2021, "Syntax Scoring") {

    override fun part1() = input.map { it.checkSyntax() }
        .filterIsInstance<Check.Corrupted>()
        .sumOf { (found, _) -> illegalScore[found]!! }

    override fun part2() = input.map { it.checkSyntax() }
        .filterIsInstance<Check.Incomplete>()
        .map { (expected) ->
            expected.reversed().fold(0L) { score, c -> score * 5 + completionScore[c]!! }
        }.sorted().let {
            it[(it.size - 1) / 2]
        }

    sealed class Check {
        data class Corrupted(val found: Char, val expected: Char?) : Check()
        data class Incomplete(val expected: List<Char>) : Check()
    }

    companion object {
        private fun String.checkSyntax(): Check {
            val stack = mutableListOf<Char>()
            for (c in this) {
                when (c) {
                    in closing -> stack += closing[c]!!
                    stack.lastOrNull() -> stack.removeLast()
                    else -> return Check.Corrupted(c, stack.lastOrNull())
                }
            }
            return Check.Incomplete(stack)
        }

        private val closing = mapOf(
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
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
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