import utils.combinations
import kotlin.math.absoluteValue

class Day19 : Day(19, 2021, "Beacon Scanner") {

    val sc: List<Scanner> = inputChunks.mapIndexed { n, l ->
        Scanner(n, l.drop(1).map {
            it.extractAllIntegers().let { (x, y, z) -> Point3D(x, y, z) }
        }.toSet())
    }.show()

    fun <T> Sequence<T>.hasAtLeast(n: Int): Boolean = take(n).count() == n

    fun overlap(a: Collection<Point3D>, b: Scanner): Triple<Scanner, Orientation, Point3D>? {
        //println("Scanner ${a.n} to ${b.n}...")
        for (orientation in Orientation.all) {
            val bx = b.changeOrientation(orientation)
            for (a1 in a) {
                for (b1 in bx.beacons) {
                    val offset = a1 - b1
                    val bxShifted = bx.changeOffset(offset)
                    val overlap = a.asSequence().filter { it in bxShifted.beacons }.hasAtLeast(12)
                    if (overlap) {
                        //println("Found relevant offset of $offset")
                        return Triple(bxShifted, orientation, offset * -1)
                    }
                }

            }
        }
//        outer@ for (a1 in a.beacons) {
//            for (bx in b.allRotations) {
//                for (b1 in bx.originalBeacons) {
//                    val offset = b1 - a1
//
//                    val offsetB = bx.originalBeacons.map {
//                        it - offset
//                    }
//
//                    //val count = a.beacons.asSequence().map { it in offsetB }.filter{ it }.take(12).count()
//                    val count = a.beacons.count { it in offsetB }
//                    if (count >= 12) {
//                        println("Found relevant offset of $offset with $count beacons")
//
//                        return Triple(bx, offset * -1, count)
//                    }
//                }
//            }
//        }
        return null
    }

    override fun part1(): Any {
//        val combinations = sc.combinations(2).toList()
//        val cluster = mutableListOf<List<Scanner>>()
//        println("Checking ${combinations.size} combinations...")
//        val t = measureTimeMillis {
//            var c = 0
//            combinations.forEach { (a, b) ->
//                val overlap = overlap(a, b)
//                if (overlap != null) {
//                    println("${a.n} overlaps with ${b.n}")
//                    cluster += listOf(a, overlap.first)
//                }
//                c++
//                println("$c of ${combinations.size}")
//            }
//        }
//        println("Done in $t ms.")
//        println("Combining ${cluster.size} clusters.")
//
//        while (cluster.size > 1) {
//            val (c, c1, c2) = cluster.combinations(2).mapNotNull { (c1, c2) ->
//                val intersecting = c1.map { it.n } intersect c2.map { it.n }.toSet()
//                if (intersecting.isNotEmpty())
//                    Triple(intersecting.first(), c1, c2)
//                else null
//            }.firstOrNull() ?: error("No more intersections found")
//
//            println("Combining $c1 with $c2")
//            val sc1 = c1.first { it.n == c }
//            val sc2 = c2.first { it.n == c }
//
//            println(sc1)
//            println(sc2)
//
//            val or = sc2.orientation - sc1.orientation
//            val offset = sc2.pos - sc1.pos
//
//            println("$or - $offset")
//            val newCluster = c1 + c2.filter { it.n !in c1.map { it.n } }.map { it.changeOrientation(or).changeOffset(offset) }
//
//            println(newCluster)
//
//            cluster -= c1
//            cluster -= c2
//            cluster += newCluster
//        }
//        val result = cluster.single()
//        println("Resulting cluster has ${result.map { it.n }}")
//
//        allScanners = result
//        return result.flatMap { it.beacons }.toSet().size


        return allScanners.flatMapTo(mutableSetOf()) { it.beacons }.size
    }

    override fun part2() =
        allScanners.combinations(2).map { (a, b) ->
            a.pos manhattanDistanceTo b.pos
        }.maxOrNull()!!

    private val allScanners: List<Scanner> by lazy {
        val fixed = sc.take(1).toMutableList()
        val open = sc.drop(1).toMutableList()

        val beaconsTotal = fixed.single().beacons.toMutableSet()
        while (open.isNotEmpty()) {
            println("Fixed: ${fixed.size} with ${beaconsTotal.size} beacons - open ${open.size}")
            //   for (f in fixed) {
            val matched = mutableListOf<Scanner>()
            for (o in open) {
                overlap(beaconsTotal, o)?.let { r ->
                    println("Scanner ${o.n} found relative to 0 at ${r.second} with offset ${r.third}!")
                    val fixedScanner = r.first
                    matched += o
                    fixed += fixedScanner
                    beaconsTotal += fixedScanner.beacons
                }
            }
            open -= matched.toSet()
            if (matched.isEmpty()) error("no matches")
            //if (matched.isNotEmpty()) break
            //    }
        }

        println("Matched ${fixed.size} scanners")
        fixed
    }
}

data class Orientation(val rotX: Int, val rotY: Int, val rotZ: Int) {
    companion object {
        val default = Orientation(0, 0, 0)
        val all: List<Orientation> =
            (0..3).flatMap { rot ->
                (0..5).map { facing ->
                    when (facing) {
                        0 -> Orientation(rot, 0, 0)
                        1 -> Orientation(rot, 0, 1)
                        2 -> Orientation(rot, 0, 2)
                        3 -> Orientation(rot, 0, 3)
                        4 -> Orientation(rot, 1, 0)
                        5 -> Orientation(rot, 3, 0)
                        else -> error("")
                    }
                }
            }.also { println(it.size) }
    }
}

operator fun Orientation.plus(other: Orientation) =
    Orientation((rotX + other.rotX) % 4,
        (rotY + other.rotY) % 4,
        (rotZ + other.rotZ) % 4)

operator fun Orientation.minus(other: Orientation) =
    Orientation((rotX - other.rotX).mod(4),
        (rotY - other.rotY).mod(4),
        (rotZ - other.rotZ).mod(4))

val origin3D = Point3D(0, 0, 0)

data class Scanner(
    val n: Int,
    private val originalBeacons: Set<Point3D>,
    val pos: Point3D = origin3D,
    val orientation: Orientation = Orientation.default,
    private val beaconCache: MutableMap<Orientation, Collection<Point3D>> = mutableMapOf(Orientation.default to originalBeacons),
) {
    val beacons: Set<Point3D> by lazy {
        if (orientation == Orientation.default && pos == origin3D)
            originalBeacons
        else
            beaconCache.getOrPut(orientation) { calcRotation(orientation) }.mapTo(mutableSetOf()) { it + pos }
    }

    fun changeOrientation(newOrientation: Orientation): Scanner =
        copy(orientation = orientation + newOrientation)

    fun changeOffset(offset: Point3D) =
        copy(pos = pos + offset)

    private fun calcRotation(orientation: Orientation): List<Point3D> = with(orientation) {
        originalBeacons.map { rotZM[rotZ] * (rotYM[rotY] * (rotXM[rotX] * it)) }
    }

    override fun toString() = "Scanner $n, $orientation, $pos"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Scanner

        if (n != other.n) return false

        return true
    }

    override fun hashCode(): Int {
        return n
    }
}

typealias BeaconList = List<Point3D>

typealias Point3D = Triple<Int, Int, Int>

val Point3D.x: Int get() = first
val Point3D.y: Int get() = second
val Point3D.z: Int get() = third

typealias Matrix3D = List<Point3D>

operator fun Matrix3D.times(p: Point3D): Point3D =
    Point3D(
        p.x * this[0].x + p.y * this[0].y + p.z * this[0].z,
        p.x * this[1].x + p.y * this[1].y + p.z * this[1].z,
        p.x * this[2].x + p.y * this[2].y + p.z * this[2].z,
    )

operator fun Point3D.times(n: Int) =
    Point3D(x * n, y * n, z * n)

operator fun Point3D.plus(other: Point3D) =
    Point3D(x + other.x, y + other.y, z + other.z)

operator fun Point3D.minus(other: Point3D) =
    Point3D(x - other.x, y - other.y, z - other.z)

infix fun Point3D.manhattanDistanceTo(other: Point3D) =
    (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue

fun Point3D.toList() = listOf(x, y, z)

fun main() {
    solve<Day19>(EXAMPLE, 79, 3621)
}

const val SMALL = """
--- scanner 0 ---
-1,-1,1
-2,-2,2
-3,-3,3
-2,-3,1
5,6,-4
8,0,7
"""

const val EXAMPLE = """
    --- scanner 0 ---
404,-588,-901
528,-643,409
-838,591,734
390,-675,-793
-537,-823,-458
-485,-357,347
-345,-311,381
-661,-816,-575
-876,649,763
-618,-824,-621
553,345,-567
474,580,667
-447,-329,318
-584,868,-557
544,-627,-890
564,392,-477
455,729,728
-892,524,684
-689,845,-530
423,-701,434
7,-33,-71
630,319,-379
443,580,662
-789,900,-551
459,-707,401

--- scanner 1 ---
686,422,578
605,423,415
515,917,-361
-336,658,858
95,138,22
-476,619,847
-340,-569,-846
567,-361,727
-460,603,-452
669,-402,600
729,430,532
-500,-761,534
-322,571,750
-466,-666,-811
-429,-592,574
-355,545,-477
703,-491,-529
-328,-685,520
413,935,-424
-391,539,-444
586,-435,557
-364,-763,-893
807,-499,-711
755,-354,-619
553,889,-390

--- scanner 2 ---
649,640,665
682,-795,504
-784,533,-524
-644,584,-595
-588,-843,648
-30,6,44
-674,560,763
500,723,-460
609,671,-379
-555,-800,653
-675,-892,-343
697,-426,-610
578,704,681
493,664,-388
-671,-858,530
-667,343,800
571,-461,-707
-138,-166,112
-889,563,-600
646,-828,498
640,759,510
-630,509,768
-681,-892,-333
673,-379,-804
-742,-814,-386
577,-820,562

--- scanner 3 ---
-589,542,597
605,-692,669
-500,565,-823
-660,373,557
-458,-679,-417
-488,449,543
-626,468,-788
338,-750,-386
528,-832,-391
562,-778,733
-938,-730,414
543,643,-506
-524,371,-870
407,773,750
-104,29,83
378,-903,-323
-778,-728,485
426,699,580
-438,-605,-362
-469,-447,-387
509,732,623
647,635,-688
-868,-804,481
614,-800,639
595,780,-596

--- scanner 4 ---
727,592,562
-293,-554,779
441,611,-461
-714,465,-776
-743,427,-804
-660,-479,-426
832,-632,460
927,-485,-438
408,393,-506
466,436,-512
110,16,151
-258,-428,682
-393,719,612
-211,-452,876
808,-476,-593
-575,615,604
-485,667,467
-680,325,-822
-627,-443,-432
872,-547,-609
833,512,582
807,604,487
839,-516,451
891,-625,532
-652,-548,-490
30,-46,-14
"""


const val cos0 = 1
const val cos90 = 0
const val cos180 = -1
const val cos270 = 0
const val sin0 = 0
const val sin90 = 1
const val sin180 = 0
const val sin270 = -1


val rotXM: List<Matrix3D> = listOf(
    listOf(
        Point3D(1, 0, 0),
        Point3D(0, 1, 0),
        Point3D(0, 0, 1),
    ),
    listOf(
        Point3D(1, 0, 0),
        Point3D(0, cos90, -sin90),
        Point3D(0, sin90, cos90),
    ),
    listOf(
        Point3D(1, 0, 0),
        Point3D(0, cos180, -sin180),
        Point3D(0, sin180, cos180),
    ),
    listOf(
        Point3D(1, 0, 0),
        Point3D(0, cos270, -sin270),
        Point3D(0, sin270, cos270),
    ),
)

val rotYM: List<Matrix3D> = listOf(
    listOf(
        Point3D(cos0, 0, sin0),
        Point3D(0, 1, 0),
        Point3D(-sin0, 0, cos0),
    ),
    listOf(
        Point3D(cos90, 0, sin90),
        Point3D(0, 1, 0),
        Point3D(-sin90, 0, cos90),
    ),
    listOf(
        Point3D(cos180, 0, sin180),
        Point3D(0, 1, 0),
        Point3D(-sin180, 0, cos180),
    ),
    listOf(
        Point3D(cos270, 0, sin270),
        Point3D(0, 1, 0),
        Point3D(-sin270, 0, cos270),
    ),
)

val rotZM: List<Matrix3D> = listOf(
    listOf(
        Point3D(cos0, -sin0, 0),
        Point3D(sin0, cos0, 0),
        Point3D(0, 0, 1),
    ),
    listOf(
        Point3D(cos90, -sin90, 0),
        Point3D(sin90, cos90, 0),
        Point3D(0, 0, 1),
    ),
    listOf(
        Point3D(cos180, -sin180, 0),
        Point3D(sin180, cos180, 0),
        Point3D(0, 0, 1),
    ),
    listOf(
        Point3D(cos270, -sin270, 0),
        Point3D(sin270, cos270, 0),
        Point3D(0, 0, 1),
    ),
)
