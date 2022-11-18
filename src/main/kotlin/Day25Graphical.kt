import utils.*
import java.awt.Color

class Day25Graphical : PixelGameEngine() {

    val day25 = Day25()

    val area = day25.seaFloor.area
    var prevState = Day25.State(area, emptySet(), emptySet())
    var state = with(day25.seaFloor) {
        Day25.State(
            area,
            area.allPoints().filter { this[it] == '>' }.toSet(),
            area.allPoints().filter { this[it] == 'v' }.toSet(),
        )
    }

    override fun onCreate() {
        construct(day25.seaFloor.area.width, day25.seaFloor.area.height, 5, 5, "AoC 2021 Day 25")
        limitFps = 20
    }

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        appInfo = "step #${frame}"
        area.forEach {
            val prevColor = when (it) {
                in prevState.east -> Color.YELLOW
                in prevState.south -> Color.CYAN
                else -> Color.BLACK
            }
            val nextColor = when (it) {
                in state.east -> Color.YELLOW
                in state.south -> Color.CYAN
                else -> Color.BLACK
            }
            if (nextColor != prevColor)
                draw(it.x, it.y, nextColor)
        }
        val nextState = with(day25) {
            state.step()
        }
        if (nextState == state)
            stop()
        prevState = state
        state = nextState
    }

}


fun main() {
    Day25Graphical().start()
}