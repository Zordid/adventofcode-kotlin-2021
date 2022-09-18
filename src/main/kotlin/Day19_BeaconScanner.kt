import utils.combinations
import utils.dim3d.*
import kotlin.math.absoluteValue

class Day19 : Day(19, 2021, "Beacon Scanner") {

    private val rawScanners: List<Scanner> = inputChunks.mapIndexed { n, l ->
        Scanner(n, l.drop(1).map {
            it.extractAllIntegers().let { (x, y, z) -> Point3D(x, y, z) }
        }.toSet())
    }.show()

    private val arrangedScanners: List<Scanner> by lazy { locateAllScanners() }

    override fun part1() =
        arrangedScanners.flatMapTo(mutableSetOf()) { it.beacons }.size

    override fun part2() =
        arrangedScanners.combinations(2).map { (a, b) ->
            a.pos manhattanDistanceTo b.pos
        }.maxOrNull()!!

    private fun locateAllScanners(): List<Scanner> {
        val origin = rawScanners.first()
        val open = rawScanners.drop(1).toMutableSet()

        fun Scanner.attachAllOverlapping(
            open: MutableSet<Scanner>,
            result: MutableList<Scanner> = mutableListOf(this),
        ): List<Scanner> {
            var next = open.firstNotNullOfOrNull { it.locateToReference(this) }
            while (next != null) {
                open -= next
                result += next
                next.attachAllOverlapping(open, result)
                next = open.firstNotNullOfOrNull { it.locateToReference(this) }
            }
            return result
        }

        return origin.attachAllOverlapping(open)
    }

}

fun main() {
    solve<Day19>(EXAMPLE, 79, 3621)
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
    Orientation(
        (rotX + other.rotX) % 4,
        (rotY + other.rotY) % 4,
        (rotZ + other.rotZ) % 4
    )

operator fun Orientation.minus(other: Orientation) =
    Orientation(
        (rotX - other.rotX).mod(4),
        (rotY - other.rotY).mod(4),
        (rotZ - other.rotZ).mod(4)
    )

val origin3D = Point3D(0, 0, 0)

fun Scanner.locateToReference(reference: Scanner): Scanner? {
    val other = this
    val commonFeatures = reference.features.keys intersect other.features.keys
    val commonFeatureCounts = commonFeatures.sumOf { minOf(reference.features[it]!!, other.features[it]!!) }
    if (commonFeatureCounts >= 66)
        for (orientation in Orientation.all) {
            val turned = other.addOrientation(orientation)
            for (pA in reference.beacons) {
                for (pB in turned.beacons) {
                    val offset = pA - pB
                    val turnedAndShifted = turned.withPosition(offset)
                    val overlap =
                        reference.beacons.asSequence().filter { it in turnedAndShifted.beacons }.hasAtLeast(12)
                    if (overlap)
                        return turnedAndShifted
                }
            }
        }
    return null
}

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

    val features: Map<Point3D, Int> = originalBeacons.combinations(2).map { (a, b) ->
        listOf((a.x - b.x).absoluteValue, (a.y - b.y).absoluteValue, (a.z - b.z).absoluteValue).sorted()
            .let { (u, v, w) -> Triple(u, v, w) }
    }.groupingBy { it }.eachCount()

    fun addOrientation(newOrientation: Orientation): Scanner =
        copy(orientation = orientation + newOrientation)

    fun withPosition(pos: Point3D) = copy(pos = pos)

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

private fun <T> Sequence<T>.hasAtLeast(n: Int): Boolean = take(n).count() == n

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
