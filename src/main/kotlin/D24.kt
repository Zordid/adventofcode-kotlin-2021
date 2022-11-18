fun code(zStart: Long, v4: Int, v5: Int, v15: Int, input: Int): Long {
    var x = 0L
    var y = 0L
    var z = zStart
    var w = 0L

    // inp w
    w = input.toLong()
    // mul x 0
    x *= 0
    // add x z
    x += z
    //mod x 26
    x %= 26
    // div z 1
    z /= v4
    // add x 11
    x += v5
    //eql x w
    x = if (x == w) 1L else 0L
    //eql x 0
    x = if (x == 0L) 1L else 0L
    //mul y 0
    y *= 0
    //add y 25
    y += 25
    // mul y x
    y *= x
    // add y 1
    y += 1
    //mul z y
    z *= y
    //mul y 0
    y *= 0
    //add y w
    y += w
    //add y 8
    y += v15
    //mul y x
    y *= x
    //add z y
    z += y
    return z
}


val puzzle = listOf(
    listOf(1, 1, 1, 26, 1, 1, 26, 1, 26, 1, 26, 26, 26, 26),
    listOf(11, 12, 10, -8, 15, 15, -11, 10, -3, 15, -3, -1, -10, -16),
    listOf(8, 8, 12, 10, 2, 8, 4, 9, 10, 3, 7, 7, 2, 2)
)

fun dec(n: IntArray) {
    var carry = false
    for (i in n.indices.reversed()) {
        if (n[i] > 0) {
            n[i]--
            return
        }
        n[i] = 9
    }
}


fun main() {
    val v4i = puzzle[0]
    val v5i = puzzle[1]
    val v15i = puzzle[2]

    val test = "99598963999971".toList().map { it.digitToInt() }
    while (true) {
        val input = test.iterator()
        var z = 0L
        println(test)
        repeat(14) {
            val n = input.next()
            z = code(z, v4i[it], v5i[it], v15i[it], n)
            println("${it+1}: $n => $z")
        }
        //dec(test)
        break
    }

}