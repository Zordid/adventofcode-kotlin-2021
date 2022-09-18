import utils.permutations

class Day08 : Day(8, 2021, "Seven Segment Search") {

    private val entries = mappedInput {
        val (sp, ov) = it.split(" | ")
        sp.split(" ").map { it.sorted() } to ov.split(" ").map { it.sorted() }
    }

    override fun part1() = entries.sumOf { (_, ov) ->
        ov.count { it.length !in listOf(5, 6) }
    }

    override fun part2() = part2UsingPermutations()

    private fun part2UsingDeduction() = entries.sumOf { (sp, ov) ->
        val all = (sp + ov).distinct()

        val known = Array<String?>(10) { null }
        known[1] = all.single { it.length == 2 }
        known[7] = all.single { it.length == 3 }
        known[4] = all.single { it.length == 4 }
        known[8] = all.single { it.length == 7 }

        all.sortedByDescending { it.length }.forEach { u ->
            when (u.length) {
                6 -> when { // 0, 6 or 9
                    !(known[1] allIn u) -> 6  // if a 1 is not contained, it's a 6
                    known[4] allIn u -> 9     // if a 4 is contained, it's a 9
                    else -> 0                 // must be a 0
                }

                5 -> when { // 2, 3 or 5
                    known[1] allIn u -> 3     // if a 1 is contained, it's a 3
                    u allIn known[9] -> 5     // if it is contained in 9, it's a 5
                    else -> 2                 // must be a 2
                }

                else -> null
            }?.let { known[it] = u }
        }

        ov.joinToString("") { known.indexOf(it).toString() }.toInt()
    }

    private fun part2UsingPermutations() = entries.sumOf { (sp, ov) ->
        val all = (sp + ov).distinct()
        val perm = "abcdefg".permutations()
        val solution = perm.first { w ->
            all.all { it.translated(w).toDigit() != null }
        }
        ov.joinToString("") { it.translated(solution).toDigit()!! }.toInt()
    }

    companion object {
        private fun String.sorted(): String =
            toList().sorted().joinToString("")

        private infix fun String?.allIn(other: String?) =
            if (this != null && other != null) all { it in other } else error("info is missing")

        private fun String.translated(wiring: String): String =
            this.map { incoming -> wiring[incoming - 'a'] }.joinToString("")

        private fun String.toDigit(): String? = when (this.sorted()) {
            "abcefg" -> 0
            "cf" -> 1
            "acdeg" -> 2
            "acdfg" -> 3
            "bcdf" -> 4
            "abdfg" -> 5
            "abdefg" -> 6
            "acf" -> 7
            "abcdefg" -> 8
            "abcdfg" -> 9
            else -> null
        }?.toString()
    }

}

fun main() {
    solve<Day08>(
        """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent(), 26, 61229
    )
}