import utils.combinations

sealed class SnailFishNumber {
    abstract fun magnitude(): Int
}
class SnailPair(var left: SnailFishNumber, var right: SnailFishNumber) : SnailFishNumber() {
    override fun magnitude(): Int {
        return 3*left.magnitude()+2*right.magnitude()
    }

    override fun toString(): String {
        return "[$left,$right]"
    }
}

class SnailNumber(var value: Int) : SnailFishNumber() {
    override fun magnitude(): Int {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }
}

fun SnailFishNumber.addFirst(v: Int?): SnailFishNumber {
    v ?: return this
    return when (this) {
        is SnailNumber -> SnailNumber(value + v)
        is SnailPair -> SnailPair(left.addFirst(v), right)
    }
}

fun SnailFishNumber.addLast(v: Int?): SnailFishNumber {
    v ?: return this
    return when (this) {
        is SnailNumber -> SnailNumber(value + v)
        is SnailPair -> SnailPair(left, right.addLast(v))
    }
}

fun SnailFishNumber.explode(l: Int = 0): Triple<SnailFishNumber, Int?, Int?> {
    println("Level $l: $this")
    if (this !is SnailPair) return Triple(this, null, null)
    if (l == 4) {
        println("BOOM")
        return Triple(SnailNumber(0), (left as SnailNumber).value, (right as SnailNumber).value)
    }
    val (lnew, leftOut, rightInner) = left.explode(l + 1)
    if (leftOut != null || rightInner != null) {
        println("got BOOM from left above $leftOut, $rightInner")
        return Triple(SnailPair(lnew, right.addFirst(rightInner)), leftOut, null)
    }
    val (rnew, leftInner, rightOut) = right.explode(l + 1)
    if (leftInner != null || rightOut != null) {
        println("got BOOM from right above $leftInner, $rightOut")
        return Triple(SnailPair(left.addLast(leftInner), rnew), null, rightOut)
    }
    return this.explode(l+1)
}

fun SnailFishNumber.split(): Pair<SnailFishNumber, Boolean> {
    return when {
        this is SnailNumber && value >= 10 -> return SnailPair(SnailNumber(value / 2),
            SnailNumber(value - value / 2)) to true
        this is SnailPair -> {
            val (l, s) = left.split()
            if (s)
                SnailPair(l, right) to true
            else {
                val (r, s) = right.split()
                if (s)
                    SnailPair(left, r) to true
                else this to false
            }
        }
        else -> this to false
    }
}

fun String.explode(): String {

    fun String.addLeft(v: Int): String {
        val n = reversed().dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.reversed()
        if (n.isNotEmpty()) {
            val pos = this.lastIndexOf(n)
            return substring(0, pos) + substring(pos).replace(n, (n.toInt() + v).toString())
        }
        return this
    }

    fun String.addRight(v: Int): String {
        val n = dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }
        if (n.isNotEmpty()) {
            val pos = indexOf(n)
            return substring(0,pos+n.length).replace(n, (n.toInt()+v).toString())+substring(pos+n.length)
        }
        return this
    }

    var p = 0
    var l = 0
    while (p < length) {
        if (this[p] == '[') {
            l++
            if (l == 5) {
                val pair = drop(p + 1).takeWhile { it != ']' }
                val (l, r) = pair.split(",")
                val leftPart = this.substring(0, p)
                val rightPart = substring(p + 2 + pair.length)
                return leftPart.addLeft(l.toInt()) + "0" + rightPart.addRight(r.toInt())
            }
        } else if (this[p] == ']') l--
        p++
    }
    return this
}

fun SnailFishNumber.reduce(): SnailFishNumber {
    var here = this
    while (true) {
        val exploded = here.toString().explode()
        if (exploded != here.toString()) {
            here = exploded.getPairs().first
            continue
        }

        val (splitted, s) = here.split()
        if (!s) break
        here = splitted
    }
    return here
}

operator fun SnailFishNumber.plus(other: SnailFishNumber): SnailFishNumber {
    val sum = SnailPair(this, other).reduce()
    return sum
}

fun String.getPairs(): Pair<SnailFishNumber, Int> {
    return when (this[0]) {
        '[' -> drop(1).getPairs().let { (left, consumedLeft) ->
            require(this[1 + consumedLeft] == ',') { error("$consumedLeft in $this is not comma") }
            val (right, consumedRight) = drop(2 + consumedLeft).getPairs()
            require(this[consumedLeft + 2 + consumedRight] == ']')
            SnailPair(left, right) to consumedLeft + consumedRight + 3
        }
        in '0'..'9' -> takeWhile { it.isDigit() }.let {
            SnailNumber(it.toInt()) to it.length
        }
        else -> error(this)
    }
}

class Day18 : Day(18, 2021) {
    val p = input



    override fun part1(): Any {

        val result = p.map {
            it.getPairs().first
        }.reduce(SnailFishNumber::plus)
        println(result)

        return result.magnitude()
    }

    override fun part2(): Any {
        val numbers = p.map {
            it.getPairs().first
        }

        return numbers.combinations(2).flatMap {
            sequenceOf(it, it.reversed())
        }.map { (a,b)->(a+b).magnitude() }.maxOrNull()!!
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