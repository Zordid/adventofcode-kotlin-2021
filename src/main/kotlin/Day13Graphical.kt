import utils.*
import java.awt.Color

class Day13Graphical : PixelGameEngine() {
    val day13 = Day13()
    val folds = day13.folds
    val initialPaper = day13.paper

    val initialArea = initialPaper.boundingArea()!!

    var nextFold = 0
    var paper = initialPaper

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        if (frame > 0L) sleep(1000)

        val area = paper.boundingArea()!!
        val pixelSize = minOf(initialArea.width / area.width, initialArea.height / area.height)
            .coerceAtMost(10)
        val offsetX = (initialArea.width - area.width * pixelSize) / 2
        val offsetY = (initialArea.height - area.height * pixelSize) / 2

        clear(Color.BLACK)
        area.forEach { p ->
            val color = if (p in paper) Color.RED else Color.BLACK
            fillRect(offsetX + p.x * pixelSize, offsetY + p.y * pixelSize, pixelSize, pixelSize, color)
        }
        if (nextFold in folds.indices)
            with(day13) {
                val fold = folds[nextFold++]
                if (fold.xy == 'x')
                    drawLine(
                        offsetX + fold.line * pixelSize,
                        offsetY,
                        offsetX + fold.line * pixelSize,
                        offsetY + area.height * pixelSize,
                        pattern = 0xAAAAAAAA
                    )
                else
                    drawLine(
                        offsetX,
                        offsetY + fold.line * pixelSize,
                        offsetX + area.width * pixelSize,
                        offsetY + fold.line * pixelSize,
                        pattern = 0xAAAAAAAA
                    )

                this@Day13Graphical.paper = this@Day13Graphical.paper.fold(fold)
            }
        else {
            nextFold = 0
            paper = initialPaper
            hold(10000)
        }
    }

}

fun main() {
    with(Day13Graphical()) {
        construct(initialArea.width, initialArea.height, 1, 1)
        start()
    }
}