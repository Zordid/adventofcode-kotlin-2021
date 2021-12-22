import utils.PixelGameEngine

class Day20Graphical:PixelGameEngine() {

    val day20 = Day20()

    override fun onUpdate(elapsedTime: Long, frame: Long) {
        super.onUpdate(elapsedTime, frame)
    }

}

fun main() {
    with(Day20Graphical()) {
        construct(100,100,1,1)
        start()
    }
}