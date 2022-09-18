import utils.*
import java.awt.Color

class Day15Graphical : PixelGameEngine() {
    val day15 = Day15()
    val area = day15.area.scale(5)
    val map = day15.graphBig

    val riskColor = listOf(Color.BLACK) + createGradient(Color.BLACK, Color.RED, 9)

    val aStar = AStar(area.upperLeft + (area.width / 4 to area.height / 4),
        { p -> p.directNeighbors() },
        { from, to -> if (to in area) map.cost(from, to) else 1000000 },
        { from, to -> from manhattanDistanceTo to }
    )

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        if (frame == 0L) {
            area.forEach { p ->
                draw(p.x, p.y, riskColor[map.cost(origin, p)])
            }
            hold(2000)
        } else {
            val r = aStar.search(area.lowerRight, 100)
            r.distance.keys.forEach {
                draw(it.x, it.y, Color.LIGHT_GRAY)
            }
            val stack = r.buildStack()
            stack.forEach {
                draw(it.x, it.y, Color.CYAN)
            }
            if (r.currentNode == area.lowerRight) stop()
        }
    }

}

fun main() {
    with(Day15Graphical()) {
        construct(area.width, area.height, 1, 1)
        //limitFps = 120
        start()
    }
}