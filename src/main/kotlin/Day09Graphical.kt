import utils.*
import java.awt.Color

class Day09Graphical : PixelGameEngine() {

    val day09 = Day09()
    val bounds = day09.bounds
    private val heights = day09.heights
    private val lows = bounds.allPoints().filter { day09.isLow(it) }
    private val basins = lows.associateWith { l ->
        day09.floodFill(l, LinkedHashSet()).iterator()
    }
    private val colors = createGradient(Color.BLUE, Color.RED, 10)

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        if (frame == 0L) {
            bounds.forEach {
                val c = colors[heights[it]!!]
                draw(it.x, it.y, c)
            }
            sleep(1000)
        } else if (frame in 1L..20L) {
            lows.forEach {
                val c = colors[heights[it]!!]
                if (frame % 2 == 0L)
                    draw(it.x, it.y, Color.WHITE)
                else
                    draw(it.x, it.y, c)
            }
            sleep(250)
        } else if (frame > 20L) {
            basins.forEach { (_, i) ->
                if (i.hasNext()) {
                    val next = i.next()
                    draw(next.x, next.y, Color.WHITE)
                }
            }
            if (basins.none { (_, i) -> i.hasNext() })
                stop()
            else
                sleep(50)
        }
    }

}

fun main() {
    with(Day09Graphical()) {
        construct(bounds.width, bounds.height, 5, 5, day09.title)
        start()
    }
}