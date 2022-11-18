import utils.product

class Day24 : Day(24, 2021) {

    val p = input

    fun solveCode(opIndex: Int = 0, wValue: Long = 0, xValue: Long = 0, yValue: Long = 0, zValue: Long = 0): String? {
        var w = wValue
        var x = xValue
        var y = yValue
        var z = zValue
        var idx = opIndex
        while (idx <= p.lastIndex) {
            val (op, arg) = p[idx++].split(" ").let { it.first() to it.drop(1) }
            val argV = arg.map {
                it.toLongOrNull() ?: when (it) {
                    "w" -> w
                    "x" -> x
                    "y" -> y
                    "z" -> z
                    else -> error("arg read error $it")
                }
            }
            when (op) {
                "inp" -> {
                    if (zValue > 1000000) return null
                    for (v in 1L .. 9L) {
                        solveCode(idx, v, x, y, z)?.let {
                            return "$v$it"
                        }
                    }
                    return null
                }
                "add" -> argV.sum()
                "mul" -> argV.product()
                "div" -> argV[0] / argV[1]
                "mod" -> argV[0] % argV[1]
                "eql" -> if (argV[0] == argV[1]) 1L else 0L
                else -> error(p)
            }.let {
                when (arg[0]) {
                    "w" -> w = it
                    "x" -> x = it
                    "y" -> y = it
                    "z" -> z = it
                    else -> error("write to $arg[0]")
                }
            }
        }
        return if (z == 0L) "" else null
    }

    fun run(prg: List<String>, input: List<Int>): Map<String, Long> {
        val nextInput = input.iterator()
        var w = 0L
        var x = 0L
        var y = 0L
        var z = 0L
        for (p in prg) {

            val (op, arg) = p.split(" ").let { it.first() to it.drop(1) }

            //println("$op $arg")
            val argV = arg.map {
                it.toLongOrNull() ?: when (it) {
                    "w" -> w
                    "x" -> x
                    "y" -> y
                    "z" -> z
                    else -> error("arg read error $it")
                }
            }
            //println("--> $op $argV")
            when (op) {
                "inp" -> nextInput.next().toLong()
                "add" -> argV.sum()
                "mul" -> argV.product()
                "div" -> argV[0] / argV[1]
                "mod" -> argV[0] % argV[1]
                "eql" -> if (argV[0] == argV[1]) 1L else 0L
                else -> error(p)
            }.let {
                when (arg[0]) {
                    "w" -> w = it
                    "x" -> x = it
                    "y" -> y = it
                    "z" -> z = it
                    else -> error("write to $arg[0]")
                }
            }
        }
        return mapOf(
            "w" to w,
            "x" to x,
            "y" to y,
            "z" to z
        )
    }

    fun code(v4: List<Int>, v5: List<Int>, v15: List<Int>, input: List<Int>): Long {
        var x = 0L
        var z = 0L
        repeat(14) { r ->
            // inp w
            // mul x 0
            // add x z
            // mod x 26
            // div z {V4}
            z /= v4[r]
            // add x {V5}
            x = z % 26 + v5[r]
            //eql x w
            //eql x 0
            if (x != input[r].toLong()) {
                //mul y 0
                //add y 25
                // mul y x
                // add y 1
                //mul z y
                //mul y 0
                //add y w
                //add y {V15}
                //mul y x
                //add z y
                z *= 26
                z += input[r].toLong() + v15[r]
            }
        }
        return z
    }

    val puzzle = listOf(
        listOf(1, 1, 1, 26, 1, 1, 26, 1, 26, 1, 26, 26, 26, 26),
        listOf(11, 12, 10, -8, 15, 15, -11, 10, -3, 15, -3, -1, -10, -16),
        listOf(8, 8, 12, 10, 2, 8, 4, 9, 10, 3, 7, 7, 2, 2)
    )

    fun testByMachine(num: List<Int>): Long =
        run(p, num)["z"]!!

    fun test(num: List<Int>): Long =
        code(puzzle[0], puzzle[1], puzzle[2], num)

    fun dec(n: IntArray) {
        var carry = false
        for (i in n.indices.reversed()) {
            if (n[i] > 1) {
                n[i]--
                return
            }
            n[i] = 9
        }
    }

    override fun part1(): Any {

        return solveCode()!!


        // val num = IntArray(14) { 9 }
        val num = "9".repeat(14).toList().map { it.digitToInt() }.toIntArray()
        //val num = "99598963999999".toList().map { it.digitToInt() }.toIntArray()
        var c = 0L
        while (true) {
            if (c++ % 1_000_000L == 0L)
                println(num.asList().joinToString(""))
            val zCode = test(num.asList())
            //val zMachine = testByMachine(num.asList())
//            if (zCode != zMachine)
//                error("$zCode vs $zMachine")
            if (zCode == 0L) {
                return num.asList().joinToString("")
            }
            dec(num)
        }
        error("nf")
    }

    override fun part2(): Any {
        return super.part2()
    }
}

fun main() {
    solve<Day24>()
}