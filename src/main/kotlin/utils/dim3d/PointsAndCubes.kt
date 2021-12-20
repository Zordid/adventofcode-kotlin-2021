package utils.dim3d

import kotlin.math.absoluteValue

typealias Point3D = Triple<Int, Int, Int>
typealias Matrix3D = List<Point3D>

val Point3D.x: Int get() = first
val Point3D.y: Int get() = second
val Point3D.z: Int get() = third

operator fun Point3D.plus(other: Point3D) =
    Point3D(x + other.x, y + other.y, z + other.z)

operator fun Point3D.minus(other: Point3D) =
    Point3D(x - other.x, y - other.y, z - other.z)

operator fun Point3D.times(n: Int) =
    Point3D(x * n, y * n, z * n)

val Point3D.manhattanDistance: Int
    get() = x.absoluteValue + y.absoluteValue + z.absoluteValue

infix fun Point3D.manhattanDistanceTo(other: Point3D) =
    (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue

fun Point3D.toList() = listOf(x, y, z)

operator fun Matrix3D.times(p: Point3D): Point3D =
    Point3D(
        p.x * this[0].x + p.y * this[0].y + p.z * this[0].z,
        p.x * this[1].x + p.y * this[1].y + p.z * this[1].z,
        p.x * this[2].x + p.y * this[2].y + p.z * this[2].z,
    )

private const val cos0 = 1
private const val cos180 = -1
private const val cos90 = 0
private const val cos270 = 0
private const val sin0 = 0
private const val sin90 = 1
private const val sin180 = 0
private const val sin270 = -1

val identityMatrix3D: Matrix3D = listOf(
    Point3D(1, 0, 0),
    Point3D(0, 1, 0),
    Point3D(0, 0, 1),
)

val rotXM: List<Matrix3D> = listOf(
    identityMatrix3D,
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
    identityMatrix3D,
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
    identityMatrix3D,
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
