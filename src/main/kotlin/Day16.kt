class Day16 : Day(16, 2021) {

    val p = input.first().toList()
        .joinToString("") { it.toString().toInt(16).toString(2).padStart(4, '0') }
        .show()

    sealed class Packet(val version: Int, val typeId: Int) {
        abstract fun versionAdd(): Int

        abstract fun calc(): Long

        class Value(version: Int, typeId: Int, val value: Long) : Packet(version, typeId) {
            override fun toString(): String {
                return "Value ($version,$typeId): $value"
            }

            override fun versionAdd(): Int {
                return version
            }

            override fun calc(): Long {
                return value
            }
        }

        class Operation(version: Int, typeId: Int, val subPackets: List<Packet>) : Packet(version, typeId) {
            override fun toString(): String {
                return "Operation ($version, $typeId) contains ${subPackets.size} sub-packets:\n${
                    subPackets.joinToString("\n") { it.toString() }
                }"
            }

            override fun versionAdd(): Int {
                return version + subPackets.sumOf { it.versionAdd() }
            }

            override fun calc(): Long {
                val v = subPackets.map { it.calc() }
                return when (typeId) {
                    0 -> v.sum()
                    1 -> v.reduce(Long::times)
                    2 -> v.minOrNull()!!
                    3 -> v.maxOrNull()!!
                    5 -> if (v[0] > v[1]) 1L else 0L
                    6 -> if (v[0] < v[1]) 1L else 0L
                    7 -> if (v[0] == v[1]) 1L else 0L
                    else->error("$typeId")
                }
            }
        }
    }

    fun String.decodePackets(start: Int, limitPackets: Int = Int.MAX_VALUE): Pair<List<Packet>, Int> {
        println("Decoding ${this.substring(start)}...")
        var p = start
        val result = mutableListOf<Packet>()
        while (p < length && substring(p).any { it != '0' } && result.size < limitPackets) {
            println("p at $p / $length")
            val version = substring(p, p + 3).toInt(2)
            val typeId = substring(p + 3, p + 3 + 3).toInt(2)
            println("t $typeId v $version")
            p += 6
            result += when (typeId) {
                4 -> {
                    var goOn = '1'
                    var value = ""
                    while (goOn == '1') {
                        substring(p, p + 5).let {
                            goOn = it.first()
                            value += it.drop(1)
                        }
                        p += 5
                    }
                    val v = value.toLong(2)
                    Packet.Value(version, typeId, v)
                }
                else -> {
                    val typeLengthId = this[p++]
                    if (typeLengthId == '0') {
                        val b = substring(p, p + 15)
                        p += 15
                        val lengthOfSubPackets = b.toInt(2)
                        val subPackets = substring(p, p + lengthOfSubPackets).decodePackets(0)
                        p += lengthOfSubPackets
                        Packet.Operation(
                            version,
                            typeId,
                            subPackets.first
                        )
                    } else {
                        val b = substring(p, p + 11)
                        p += 11
                        val numberOfSubPackets = b.toInt(2)
                        val subPackets = decodePackets(p, numberOfSubPackets)
                        p += subPackets.second
                        Packet.Operation(version, typeId, subPackets.first)
                    }
                }
            }
        }
        return result to p - start
    }

    override fun part1(): Any {
        val packets = p.decodePackets(0).first




        return packets.sumOf { it.versionAdd() }
    }

    override fun part2(): Any {
        val packets = p.decodePackets(0).first
        return packets.first().calc()
    }

}

fun main() {
    solve<Day16>("""9C0141080250320F1802104A08""", null, 1L)
}