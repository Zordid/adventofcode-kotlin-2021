import utils.*
import java.awt.Color
import kotlin.math.sign

class Day17Graphical : PixelGameEngine() {

    val day17 = Day17()
    val tX = day17.targetX
    val tY = day17.targetY
    val targetBox = (tX.first to tY.first) to (tX.last to tY.last)
    val possibleV = day17.possibleVelocities

    val allTrajectories = possibleV.keys.associateWith {
        trajectory(it).toList()
    }

    val area = (listOf(origin, targetBox.first, targetBox.second) + possibleV.keys).boundingArea()!!

    val t = allTrajectories
        .filter { it.value.size == allTrajectories.values.maxOfOrNull { it.size } }
        .values
        .iterator()
    var currentTrajectory: Iterator<Point> = t.next().iterator()
    var lastPoint = origin

    override fun onUpdate(elapsedTime: Long, frame: Long) {

        fun Int.oX() = this + 5
        fun Int.oY() = (area.height - 1) - this - area.second.y

//            possibleV.forEach { (p,_)->
//                allTrajectories[p]!!.forEach {
//                    draw(it.x.oX(), it.y.oY(), Color.DARK_GRAY)
//                }
//            }

        if (!currentTrajectory.hasNext()) {
            //clear()
            //sleep(100)
        }
        if (frame == 0L)
            possibleV.forEach { (p, _) ->
                draw(p.x.oX(), p.y.oY(), Color.LIGHT_GRAY)
            }
        drawLine(0.oX(), 0.oY(), area.width.oX(), 0.oY(), Color.WHITE, 0xBBBBBBBB)
        draw(0.oX(), 0.oY(), Color.RED)
        drawRect(
            targetBox.first.x.oX(),
            targetBox.second.y.oY(),
            targetBox.width,
            targetBox.height,
            Color.RED
        )

        if (frame == 0L)
            return hold(1000)

        if (!currentTrajectory.hasNext()) {
            currentTrajectory = if (t.hasNext()) t.next().iterator() else return stop()
            lastPoint = origin
        }

        val nextPoint = currentTrajectory.next()
        require(nextPoint.x.oX() >= 5)
        require(nextPoint.x <= tX.last.oX() - 5)

        if (nextPoint != lastPoint) {
            // draw(nextPoint.x.oX(), nextPoint.y.oY(), Color.RED)
            drawLine(lastPoint.x.oX(), lastPoint.y.oY(), nextPoint.x.oX(), nextPoint.y.oY(), Color.RED)
        }
        lastPoint = nextPoint
    }

    private fun trajectory(vi: Point): Sequence<Point> = sequence {
        var p = origin
        var v = vi
        var max = 0
        while (p.x <= tX.last && p.y >= tY.first) {
            yield(p)
            if (p.x in tX && p.y in tY)
                break
            p += v
            max = maxOf(max, p.y)
            v = (v.x - v.x.sign to v.y - 1)
        }
    }

}


fun main() {
    with(Day17Graphical()) {
        construct(area.width + 10, area.height + 2, 3, 3)
        println(area)
        println(area.height)
        println(area.width)
        println()
        println(targetBox)
        println(targetBox.width)
        println(targetBox.height)

        limitFps = 30
        start()
    }
}