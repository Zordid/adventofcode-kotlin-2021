import utils.*
import java.awt.Color

class Day13Graphical : PixelGameEngine() {
    val day13 = Day13()
    val folds = day13.folds
    val initialPaper = day13.paper

    val initialArea = initialPaper.boundingArea()!!

    var nextFold = 0
    var paper = initialPaper

    override fun onCreate() {
        construct(initialArea.width, initialArea.height, appName = day13.title)
    }

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        hold(1000)
        appInfo = "$frame folds"

        val area = paper.boundingArea()!!
        val pixelSize = minOf(initialArea.width / area.width, initialArea.height / area.height)
            .coerceAtMost(10)
        val offsetX = (initialArea.width - area.width * pixelSize) / 2
        val offsetY = (initialArea.height - area.height * pixelSize) / 2

        clear(Color.BLACK)
        area.forEach { p ->
            val color = if (p in paper) Color.RED else Color.BLACK
            if (pixelSize > 1)
                fillRect(offsetX + p.x * pixelSize, offsetY + p.y * pixelSize, pixelSize, pixelSize, color)
            else
                draw(offsetX + p.x, offsetY + p.y, color)
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
            stop()
        }
    }

}

fun main() {
    Day13Graphical().start()
}