import utils.*
import java.awt.Color

class Day11Graphical : PixelGameEngine() {

    val day11 = Day11()
    var current = day11.levels
    var flashing = emptySet<Point>()
    var cycle = 0L

    val colorBand = createGradient(Color.BLACK, Color.RED, 10)

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        if (frame == 0L) {
            drawCurrent()
            return
        }

        if (frame % 12L == 0L) {
            cycle++
            appInfo = "Cycle $cycle"
            val n = with(day11) { current.step() }
            current = n.first
            flashing = n.second
            drawCurrent()
        } else {
            if (frame % 2L == 0L)
                flashing.forEach {
                    draw(it.x, it.y, Color.WHITE)
                }
            else
                flashing.forEach {
                    draw(it.x, it.y, colorBand[current[it]])
                }
        }
    }

    private fun drawCurrent() {
        current.forArea { p, v ->
            draw(p.x, p.y, colorBand[v])
        }
    }

}

fun main() {
    with(Day11Graphical()) {
        construct(10, 10, 30, 30, day11.title)
        limitFps = 100
        start()
    }
}