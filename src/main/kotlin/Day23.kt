import utils.*

class Day23 : Day(23, 2021) {

    val initialMap: Grid<Char> = mappedInput { it.toList() }
    val area = initialMap.area()
    val costs = listOf(1, 10, 100, 1000)

    val sideRoomsX = listOf(3, 5, 7, 9)
    val sideRoomsY = 2..5
    val hallwayY = 1
    val hallway = initialMap.searchFor('.')
    val WALL = '#'
    val FREE = '.'

    val initialState = State(listOf(
        initialMap.searchFor('A').sortedBy { it.y },
        initialMap.searchFor('B').sortedBy { it.y },
        initialMap.searchFor('C').sortedBy { it.y },
        initialMap.searchFor('D').sortedBy { it.y }
    ), 0)

    val amphiIndex = (0..3).map { type ->
        (0..3).map { it to type }
    }.flatten()

    fun List<List<Point>>.move(type: Int, n: Int, p: Point) =
        mapIndexed { t, a ->
            if (type == t) a.mapIndexed { i, pp -> if (i == n) p else pp }.sortedBy { it.y }
            else a
        }

    inner class State(val amphipods: List<List<Point>>, val energy: Int) {

        val occupied = amphipods.flatten()

        fun showGrid() {
            println()
            println("Energy: $energy")
            initialMap.forArea { p, x ->
                if (p.x == 0) println()
                if (x in listOf(' ', '#'))
                    print(x)
                else {
                    if (p in occupied) {
                        val (_, type) = amphipods.indices().single {
                            amphipods[it] == p
                        }
                        print(Char('A'.code + type))
                    } else print('.')
                }
            }
        }

        fun IntRange.fix() = if (isEmpty())
            last..first else this

        fun Point.freeTo(other: Point): Boolean =
            if (this.y in sideRoomsY) {
                (this.y - 1 downTo hallwayY).all { y -> Point(this.x, y) !in occupied } &&
                        (this.x..other.x).fix().all { Point(it, hallwayY) !in occupied }
            } else {
                val onHallway = (this.x..other.x).fix().all { it == this.x || Point(it, hallwayY) !in occupied }
                val toRoom = (this.y..other.y).all { Point(other.x, it) !in occupied }
                (onHallway && toRoom).also {
                    // println("Hallway check to room failed $this -> $other: $onHallway $toRoom")
                }
            }

        fun possibleMoves(): List<State> {
            //showGrid()
            val inHallway = amphiIndex.filter { amphipods[it] in hallway }
            val inRooms = amphiIndex.filter { amphipods[it] !in hallway }

            return buildList {
                rooms@ for ((n, type) in inRooms) {
                    val current = amphipods[type][n]
                    if (current.x == sideRoomsX[type]) {
                        if ((current.y + 1..sideRoomsY.last).all { y ->
                                val who = amphiIndex.filter { (n2, t2) ->
                                    amphipods[t2][n2] == current.x to y
                                }
                                who.all { (_, t) -> t == type }
                            })
                            continue@rooms
                    }

                    for (p in hallway) {
                        if (p.x !in sideRoomsX || p.x == sideRoomsX[type])
                            if (current.freeTo(p))
                                add(State(amphipods.move(type, n, p),
                                    energy + (current manhattanDistanceTo p) * costs[type]))
                    }
                }
                hallway@ for ((n, type) in inHallway) {
                    //println("Check if $type can move to destination")
                    val destX = sideRoomsX[type]
                    for (ot in 0..3) {
                        if (ot == type) continue
                        if (amphipods[ot].any { it.x == destX })
                            continue@hallway
                    }

                    val current = amphipods[type][n]

                    val firstFreeY = (sideRoomsY.last downTo sideRoomsY.first).firstOrNull { y->
                        current.freeTo(destX to y)
                    }
                    if (firstFreeY!=null)
                        add(State(amphipods.move(type, n, destX to firstFreeY),
                            energy + (current manhattanDistanceTo (destX to firstFreeY)) * costs[type]))
                }
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (amphipods != other.amphipods) return false

            return true
        }

        override fun hashCode(): Int {
            return amphipods.hashCode()
        }

    }

    override fun part1(): Any {
        val (result) = Dijkstra<State>(
            { s -> s.possibleMoves() },
            { f, t -> t.energy - f.energy }
        ).search(initialState) { s ->
            (0..3).all { type ->
                s.amphipods[type].all { p ->
                    p.x == sideRoomsX[type] && p.y in sideRoomsY
                }
            }
        }

        return result!!.energy
    }

    override fun part2(): Any {
        return super.part2()
    }

    fun Grid<Char>.searchFor(c: Char): List<Point> = sequence {
        area.forEach { p -> if (this@searchFor[p] == c) yield(p) }
    }.toList()

}

fun main() {
    solve<Day23>("""
        #############
        #...........#
        ###B#C#B#D###
          #D#C#B#A#  
          #D#B#A#C#  
          #A#D#C#A#  
          #########  
    """.trimIndent(), 44169 )
}