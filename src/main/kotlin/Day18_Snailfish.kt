import utils.combinations
import utils.permutations
import java.lang.Integer.max


class Day18 : Day(18, 2021, "Snailfish") {
    private val numbers = mappedInput { SnailFishNumber.fromString(it) }

    override fun part1() =
        numbers.reduce(SnailFishNumber::plus).magnitude

    override fun part2() =
        numbers
            .combinations(2).flatMap { it.permutations() }
            .map { it.reduce(SnailFishNumber::plus).magnitude }
            .maxOrNull()!!

}

sealed class SnailFishNumber {
    abstract val depth: Int
    abstract val magnitude: Int
    abstract fun addToLeftValue(v: Int): SnailFishNumber
    abstract fun addToRightValue(v: Int): SnailFishNumber

    data class SnailPair(var left: SnailFishNumber, var right: SnailFishNumber) : SnailFishNumber() {
        override val depth get() = max(left.depth, right.depth) + 1
        override val magnitude get() = 3 * left.magnitude + 2 * right.magnitude
        override fun addToLeftValue(v: Int) = if (v == 0) this else SnailPair(left.addToLeftValue(v), right)
        override fun addToRightValue(v: Int) = if (v == 0) this else SnailPair(left, right.addToRightValue(v))

        override fun toString() = "[$left,$right]"
    }

    data class SnailNumber(var value: Int) : SnailFishNumber() {
        override val depth = 0
        override val magnitude get() = value
        override fun addToLeftValue(v: Int) = if (v == 0) this else SnailNumber(value + v)
        override fun addToRightValue(v: Int) = if (v == 0) this else SnailNumber(value + v)
        override fun toString() = value.toString()
    }

    operator fun plus(other: SnailFishNumber) =
        SnailPair(this, other).reduce()

    fun reduce(): SnailFishNumber {
        var current = this
        do {
            current = current.explodeFully()

            val split = current.splitFirst()
            if (split == current)
                return current
            current = split
        } while (true)
    }

    private fun explodeFully(): SnailFishNumber {
        var exploded = this
        while (exploded.depth > 4)
            exploded = exploded.explode(0).second
        return exploded
    }

    private fun explode(level: Int): Triple<Int, SnailFishNumber, Int> {
        if (this !is SnailPair) return Triple(0, this, 0)

        if (level == 4)
            return Triple((left as SnailNumber).value, SnailNumber(0), (right as SnailNumber).value)

        // a pair, but not on level 4
        left.explode(level + 1).also { (leftOut, exploded, rightOut) ->
            if (exploded != left)
                return Triple(leftOut, SnailPair(exploded, right.addToLeftValue(rightOut)), 0)

        }
        right.explode(level + 1).also { (leftOut, exploded, rightOut) ->
            if (exploded != right)
                return Triple(0, SnailPair(left.addToRightValue(leftOut), exploded), rightOut)
        }

        return Triple(0, this, 0)
    }

    private fun splitFirst(): SnailFishNumber =
        when {
            this is SnailNumber && value >= 10 -> SnailPair(
                SnailNumber(value / 2),
                SnailNumber(value - value / 2)
            )
            this is SnailPair -> {
                val leftSplit = left.splitFirst()
                if (left != leftSplit)
                    SnailPair(leftSplit, right)
                else {
                    val rightSplit = right.splitFirst()
                    if (right != rightSplit) SnailPair(left, rightSplit) else this
                }
            }
            else -> this
        }


    companion object {
        fun fromString(s: String): SnailFishNumber {
            fun String.getPairs(): Pair<SnailFishNumber, Int> = when (this.first()) {
                '[' -> drop(1).getPairs().let { (left, consumedLeft) ->
                    require(this[1 + consumedLeft] == ',')
                    val (right, consumedRight) = drop(2 + consumedLeft).getPairs()
                    require(this[1 + consumedLeft + 1 + consumedRight] == ']')
                    SnailPair(left, right) to consumedLeft + consumedRight + 3
                }
                in '0'..'9' -> takeWhile { it.isDigit() }.let {
                    SnailNumber(it.toInt()) to it.length
                }
                else -> error("starts with unknown token: $this")
            }
            return s.getPairs().first
        }

    }
}

fun main() {
    solve<Day18>("""
        [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent(), 4140, 3993)
}