import utils.dim3d.Point3D
import java.math.BigInteger
import kotlin.system.exitProcess

class Day22 : Day(22, 2021) {

    val p = mappedInput {
        Region(it.startsWith("on"),
            it.extractAllIntegers().chunked(2).map { (f, t) ->
                f..t
            })
    }

    override fun part1(): Any {
        val state = mutableSetOf<Point3D>()

        p.forEach { r ->
            with(r) {
                val xrl = xr.first.coerceAtLeast(-50)..xr.last.coerceAtMost(50)
                val yrl = yr.first.coerceAtLeast(-50)..yr.last.coerceAtMost(50)
                val zrl = zr.first.coerceAtLeast(-50)..zr.last.coerceAtMost(50)

                for (x in xrl)
                    for (y in yrl)
                        for (z in zrl) {
                            if (on)
                                state += Point3D(x, y, z)
                            else
                                state -= Point3D(x, y, z)
                        }
            }
        }

        return state.size
    }

    interface Reg {
        val xr: IntRange
        val yr: IntRange
        val zr: IntRange
        val volume: Long
            get() = if (xr.isEmpty() || yr.isEmpty() || zr.isEmpty()) 0L else
                (xr.last - xr.first + 1L) *
                        (yr.last - yr.first + 1L) *
                        (zr.last - zr.first + 1L)

        fun toList() = listOf(xr, yr, zr)
    }

    data class Region(
        val on: Boolean,
        private val r: List<IntRange>,
        val contained: List<Region> = emptyList(),
    ) : Reg,
        List<IntRange> by r {
        override val xr: IntRange get() = r[0]
        override val yr: IntRange get() = r[1]
        override val zr: IntRange get() = r[2]
        val off: Boolean get() = !on

        init {
            require(contained.all { it.on == !on })
        }

        fun Iterable<BigInteger>.sum() = fold(BigInteger.ZERO) { acc, i -> acc + i }

        fun countOn(): BigInteger {
            return if (on) {
                volume.toBigInteger() - contained.map { it.countOn() }.sum()
            } else {
                contained.map { it.countOn() }.sum()
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Region

            if (on != other.on) return false
            if (r != other.r) return false

            return true
        }

        override fun hashCode(): Int {
            var result = on.hashCode()
            result = 31 * result + r.hashCode()
            return result
        }
    }

    operator fun Reg.contains(other: Reg): Boolean =
        this.xr.first >= other.xr.first && this.xr.last <= other.xr.last &&
                this.yr.first >= other.yr.first && this.yr.last <= other.yr.last &&
                this.zr.first >= other.zr.first && this.zr.last <= other.zr.last

    infix fun Reg.disjunct(other: Reg): Boolean =
        (this.xr.last < other.xr.first || other.xr.last < this.xr.first) ||
                (this.yr.last < other.yr.first || other.yr.last < this.yr.first) ||
                (this.zr.last < other.zr.first || other.zr.last < this.zr.first)

    data class R(val r: List<IntRange>) : Reg, List<IntRange> by r {
        override val xr: IntRange get() = r[0]
        override val yr: IntRange get() = r[1]
        override val zr: IntRange get() = r[2]
    }

    infix fun Reg.overlap(other: Reg): Reg {
        val xr =
            when {
                this.xr.first >= other.xr.first && this.xr.last <= other.xr.last -> this.xr
                other.xr.first >= this.xr.first && other.xr.last <= this.xr.last -> other.xr
                else -> maxOf(this.xr.first, other.xr.first)..minOf(this.xr.last, other.xr.last)
            }
        val yr =
            when {
                this.yr.first >= other.yr.first && this.yr.last <= other.yr.last -> this.yr
                other.yr.first >= this.yr.first && other.yr.last <= this.yr.last -> other.yr
                else -> maxOf(this.yr.first, other.yr.first)..minOf(this.yr.last, other.yr.last)
            }
        val zr =
            when {
                this.zr.first >= other.zr.first && this.zr.last <= other.zr.last -> this.zr
                other.zr.first >= this.zr.first && other.zr.last <= this.zr.last -> other.zr
                else -> maxOf(this.zr.first, other.zr.first)..minOf(this.zr.last, other.zr.last)
            }
        return R(listOf(xr, yr, zr))
    }

    infix fun Reg.combinedRegion(other: Reg): Reg {
        return R(listOf(
            minOf(this.xr.first, other.xr.first)..maxOf(this.xr.last, other.xr.last),
            minOf(this.yr.first, other.yr.first)..maxOf(this.yr.last, other.yr.last),
            minOf(this.zr.first, other.zr.first)..maxOf(this.zr.last, other.zr.last)
        ))
    }

    val maxRange = (Int.MIN_VALUE + 10)..(Int.MAX_VALUE - 10)

    operator fun Reg.minus(other: Reg): List<Reg> = listOf<Reg>(
        R(listOf(this.xr, this.yr, this.zr.first..other.zr.first - 1)),
        R(listOf(this.xr, this.yr, other.zr.last + 1..this.zr.last)),
        R(listOf(this.xr, this.yr.first..other.yr.first - 1, other.zr)), // 1
        R(listOf(this.xr, other.yr.last + 1..this.yr.last, other.zr)), // 2
        R(listOf(this.xr.first..other.xr.first - 1, other.yr, other.zr)), // 3
        R(listOf(other.xr.last + 1..this.xr.last, other.yr, other.zr)), // 4
    ).filter {
        it.volume != 0L
    }

    infix fun Region.reduceBy(subtract: Reg): List<Region> {
        return (this - subtract).map {
            Region(on, it.toList(), contained = contained.mapNotNull { c ->
                if (c disjunct subtract)
                    listOf(c)
                else if (c in subtract)
                    null
                else {
                    c reduceBy subtract
                }
            }.flatten())
        }
    }

    infix fun Region.combineWith(other: Region): Region {
        //println("Combining $this with $other")
        require(this in other)
        require(other.contained.isEmpty())
        when {
            on != other.on -> { // switch
                // off region, want to switch on
                if (contained.all { it in other }) // all former ons are within the new on
                    return copy(contained = listOf(other))
                if (contained.all { it disjunct other }) { // all former ons are completely disjunct to the new on
                    return copy(contained = contained + listOf(other))
                }
                println("must merge $other...")
                // there are overlaps to switch!
                return copy(contained = listOf(other) + contained.flatMap { subOn ->
                    println(subOn)
                    if (subOn disjunct other)
                        listOf(subOn).also { println("is disjunct") }
                    else {
                        val overlap = subOn overlap other
                        println("overlap region is $overlap")
                        (subOn reduceBy overlap)
                    }
                })
            }
            on == other.on -> {
                // in off Region, want to switch off
                if (contained.all { c -> c disjunct other }) {
                    return this
                }
                return copy(contained = contained.flatMap { subOn ->
                    if (subOn disjunct other)
                        listOf(subOn)
                    else {
                        val overlap = subOn overlap other
                        (subOn reduceBy overlap) //Region(!on, overlap.toList())
                    }
                })
            }
        }
        error("")
    }

    override fun part2(): Any {
//
//        val r1 = R(listOf(0..2, 0..2, 0..2))
//        val r2 = R(listOf(-1000..1000, 1..1, 1..1))
//
//        (r1 - r2).forEach(::println)
//
//        println(r1.volume)
//        println(r2.volume)
//        println((r1 - r2).sumOf(Reg::volume))
//
//
//        exitProcess(0)

        val state = Region(false, listOf(maxRange, maxRange, maxRange))

        val r = p.fold(state) { acc, step ->
            println("---")
            println("${step.on}: ${step.xr}/${step.yr}/${step.zr} ${step.volume}")
            (acc combineWith step).also { newAcc ->
                val before = acc.countOn()
                val now = newAcc.countOn()

                val v = (if (step.on) +1 else -1) * step.volume
                val op = (if (step.on) "+" else "-")
                val check = before + v.toBigInteger()
                println("$before $op $v")
                println("Must not exceed = $check")
                println("Is              = ${newAcc.countOn()} + ${newAcc.contained.size}")
                println()
                newAcc.contained.forEach { println(it);println(it.countOn()) }
                if ((step.on && now > check) || (step.off && now < check)) {
                    println("FAILURE!")
                    exitProcess(-1)
                }
            }
        }

        return r.countOn()
    }

}

fun main() {
    solve<Day22>(XLARGE.trimIndent(), null, 2758514936282235)
}

const val XLARGE = """
on x=-5..47,y=-31..22,z=-19..33
on x=-44..5,y=-27..21,z=-14..35
on x=-49..-1,y=-11..42,z=-10..38
on x=-20..34,y=-40..6,z=-44..1
off x=26..39,y=40..50,z=-2..11
on x=-41..5,y=-41..6,z=-36..8
off x=-43..-33,y=-45..-28,z=7..25
on x=-33..15,y=-32..19,z=-34..11
off x=35..47,y=-46..-34,z=-11..5
on x=-14..36,y=-6..44,z=-16..29
on x=-57795..-6158,y=29564..72030,z=20435..90618
on x=36731..105352,y=-21140..28532,z=16094..90401
on x=30999..107136,y=-53464..15513,z=8553..71215
on x=13528..83982,y=-99403..-27377,z=-24141..23996
on x=-72682..-12347,y=18159..111354,z=7391..80950
on x=-1060..80757,y=-65301..-20884,z=-103788..-16709
on x=-83015..-9461,y=-72160..-8347,z=-81239..-26856
on x=-52752..22273,y=-49450..9096,z=54442..119054
on x=-29982..40483,y=-108474..-28371,z=-24328..38471
on x=-4958..62750,y=40422..118853,z=-7672..65583
on x=55694..108686,y=-43367..46958,z=-26781..48729
on x=-98497..-18186,y=-63569..3412,z=1232..88485
on x=-726..56291,y=-62629..13224,z=18033..85226
on x=-110886..-34664,y=-81338..-8658,z=8914..63723
on x=-55829..24974,y=-16897..54165,z=-121762..-28058
on x=-65152..-11147,y=22489..91432,z=-58782..1780
on x=-120100..-32970,y=-46592..27473,z=-11695..61039
on x=-18631..37533,y=-124565..-50804,z=-35667..28308
on x=-57817..18248,y=49321..117703,z=5745..55881
on x=14781..98692,y=-1341..70827,z=15753..70151
on x=-34419..55919,y=-19626..40991,z=39015..114138
on x=-60785..11593,y=-56135..2999,z=-95368..-26915
on x=-32178..58085,y=17647..101866,z=-91405..-8878
on x=-53655..12091,y=50097..105568,z=-75335..-4862
on x=-111166..-40997,y=-71714..2688,z=5609..50954
on x=-16602..70118,y=-98693..-44401,z=5197..76897
on x=16383..101554,y=4615..83635,z=-44907..18747
off x=-95822..-15171,y=-19987..48940,z=10804..104439
on x=-89813..-14614,y=16069..88491,z=-3297..45228
on x=41075..99376,y=-20427..49978,z=-52012..13762
on x=-21330..50085,y=-17944..62733,z=-112280..-30197
on x=-16478..35915,y=36008..118594,z=-7885..47086
off x=-98156..-27851,y=-49952..43171,z=-99005..-8456
off x=2032..69770,y=-71013..4824,z=7471..94418
on x=43670..120875,y=-42068..12382,z=-24787..38892
off x=37514..111226,y=-45862..25743,z=-16714..54663
off x=25699..97951,y=-30668..59918,z=-15349..69697
off x=-44271..17935,y=-9516..60759,z=49131..112598
on x=-61695..-5813,y=40978..94975,z=8655..80240
off x=-101086..-9439,y=-7088..67543,z=33935..83858
off x=18020..114017,y=-48931..32606,z=21474..89843
off x=-77139..10506,y=-89994..-18797,z=-80..59318
off x=8476..79288,y=-75520..11602,z=-96624..-24783
on x=-47488..-1262,y=24338..100707,z=16292..72967
off x=-84341..13987,y=2429..92914,z=-90671..-1318
off x=-37810..49457,y=-71013..-7894,z=-105357..-13188
off x=-27365..46395,y=31009..98017,z=15428..76570
off x=-70369..-16548,y=22648..78696,z=-1892..86821
on x=-53470..21291,y=-120233..-33476,z=-44150..38147
off x=-93533..-4276,y=-16170..68771,z=-104985..-24507
"""

const val SMALL = """
        on x=10..12,y=10..12,z=10..12
        on x=11..13,y=11..13,z=11..13
        off x=9..11,y=9..11,z=9..11
        on x=10..10,y=10..10,z=10..10
    """

const val LARGE = """
on x=-20..26,y=-36..17,z=-47..7
on x=-20..33,y=-21..23,z=-26..28
on x=-22..28,y=-29..23,z=-38..16
on x=-46..7,y=-6..46,z=-50..-1
on x=-49..1,y=-3..46,z=-24..28
on x=2..47,y=-22..22,z=-23..27
on x=-27..23,y=-28..26,z=-21..29
on x=-39..5,y=-6..47,z=-3..44
on x=-30..21,y=-8..43,z=-13..34
on x=-22..26,y=-27..20,z=-29..19
off x=-48..-32,y=26..41,z=-47..-37
on x=-12..35,y=6..50,z=-50..-2
off x=-48..-32,y=-32..-16,z=-15..-5
on x=-18..26,y=-33..15,z=-7..46
off x=-40..-22,y=-38..-28,z=23..41
on x=-16..35,y=-41..10,z=-47..6
off x=-32..-23,y=11..30,z=-14..3
on x=-49..-5,y=-3..45,z=-29..18
off x=18..30,y=-20..-8,z=-3..13
on x=-41..9,y=-7..43,z=-33..15
on x=-54112..-39298,y=-85059..-49293,z=-27449..7877
on x=967..23432,y=45373..81175,z=27513..53682
"""