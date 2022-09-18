import utils.product

class Day16 : Day(16, 2021, "Packet Decoder") {

    private val packets = input.first().toList()
        .joinToString("") { it.toString().toInt(16).toString(2).padStart(4, '0') }
        .asBitProvider().decodePackets().single()
        .show("Packets")

    override fun part1() = packets.sumOfVersions()

    override fun part2() = packets.value

    sealed class Packet(val version: Int, val typeId: Int) {
        open fun sumOfVersions(): Int = version
        abstract val value: Long

        class Value(version: Int, typeId: Int, override val value: Long) : Packet(version, typeId)

        class Operation(version: Int, typeId: Int, private val subPackets: List<Packet>) : Packet(version, typeId) {
            override fun sumOfVersions(): Int = version + subPackets.sumOf { it.sumOfVersions() }
            override val value: Long
                get() = with(subPackets.map { it.value }) {
                    when (typeId) {
                        0 -> sum()
                        1 -> product()
                        2 -> minOrNull()!!
                        3 -> maxOrNull()!!
                        5 -> if (component1() > component2()) 1L else 0L
                        6 -> if (component1() < component2()) 1L else 0L
                        7 -> if (component1() == component2()) 1L else 0L
                        else -> error("$typeId")
                    }
                }
        }
    }

    private fun BitProvider.decodePackets(limitPackets: Int = Int.MAX_VALUE): List<Packet> {
        //println("Decoding $this...")
        val result = mutableListOf<Packet>()
        while (hasMore() && result.size < limitPackets) {
            val version = nextInt(3)
            val typeId = nextInt(3)
            result += when (typeId) {
                4 -> {
                    var value = ""
                    do {
                        val stop = next(1) == "0"
                        value += next(4)
                    } while (!stop)
                    Packet.Value(version, typeId, value.toLong(2))
                }

                else -> when (next(1)) {
                    "0" -> {
                        val lengthOfSubPackets = nextInt(15)
                        val subPackets = next(lengthOfSubPackets).asBitProvider().decodePackets()
                        Packet.Operation(version, typeId, subPackets)
                    }

                    else -> {
                        val numberOfSubPackets = nextInt(11)
                        val subPackets = decodePackets(numberOfSubPackets)
                        Packet.Operation(version, typeId, subPackets)
                    }
                }
            }
        }
        return result
    }

    class BitProvider(private val origin: String) {
        private var pos = 0

        fun hasMore(): Boolean =
            pos < origin.length && origin.substring(pos).any { it != '0' }

        fun next(n: Int): String =
            origin.substring(pos, pos + n).also { pos += n }

        fun nextInt(n: Int): Int =
            next(n).toInt(2)

        override fun toString(): String = origin.substring(pos)
    }

    private fun String.asBitProvider() = BitProvider(this)

}

fun main() {
    solve<Day16>("""9C0141080250320F1802104A08""", null, 1L)
}