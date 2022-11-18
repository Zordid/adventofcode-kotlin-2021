import utils.*
import java.awt.Color

class Day05Graphical : PixelGameEngine() {

    private val day05 = Day05()
    private val lines = day05.lines

    init {
        val area = lines.flatten().boundingArea()!!

        construct(area.width, area.height, 1, 1, day05.title)
        limitFps = 100
        println(area)
    }

    var line = 0

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        when {
            line <= lines.lastIndex -> {
                val l = lines[line++]
                drawLine(l.p1.x, l.p1.y, l.p2.x, l.p2.y)
            }
            line == lines.lastIndex + 1 -> {
                val red = lines.flatMap { it.allPoints }.groupingBy { it }.eachCount().filter { it.value > 1 }.keys
                red.forEach {
                    draw(it.x, it.y, Color.RED)
                }
                line++
            }
            else -> stop()
        }
    }

}

fun main() {
    Day05Graphical().start()
}