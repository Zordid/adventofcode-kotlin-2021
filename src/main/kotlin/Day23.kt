import utils.*

class Day23 : Day(23, 2021) {

    val initialMap: Grid<Char> = mappedInput { it.toList() }
    val area = initialMap.area
    val costs = listOf(1, 10, 100, 1000)
    val sideRoomsX = listOf(3, 5, 7, 9)
    val sideRoomsY = 2..5
    val hallwayY = 1
    val hallway = initialMap.searchIndices { it == '.' }

    data class MapLayout(val hallway: List<Point>, val rooms: List<List<Point>>) {
        val hallwayY = hallway.map { it.y }.distinct().single()
        val roomsX = rooms.map { it.first().x }.distinct().sorted()
        val roomsY = rooms.first().map { it.y }.rangeOrNull()!!

        companion object {
            fun fromInput(map: Grid<Char>): MapLayout {
                val rooms = map.searchIndices { it.isLetter() }.toSet()
                val listOfRooms = rooms.map { it.y }.distinct().sorted()
                    .map { y -> rooms.filter { it.y == y }.sortedByDescending { it.y } }
                return MapLayout(
                    map.searchIndices { it == FREE }.toList(),
                    listOfRooms
                )
            }

            private const val FREE = '.'
        }

    }

    data class Amphipod(val type: Char, val pos: Point) {
        val typeId: Int get() = type - 'A'
    }

    data class NewState(val pods: Set<Amphipod>) {

        fun isFinal(layout: MapLayout): Boolean =
            pods.all { it.pos.y > layout.hallwayY && it.pos.x == layout.roomsX[it.typeId] }

        fun possibleMoves(layout: MapLayout): List<NewState> {
            with(layout) {
                val (podsInRooms, podsInHallway) = pods.partition { it.pos.y > hallwayY }
                val openRooms = rooms.mapIndexed { idx, r ->
                    r.none { p -> pods.firstOrNull { it.pos == p && it.typeId != idx } != null }
                }
                val emptyRoomPos = rooms.mapIndexed { idx, r ->
                    r.firstOrNull { p -> pods.none { it.pos == p } }
                }
                val firstInRoom = rooms.mapIndexed { idx, r ->
                    //r.
                }

                return buildList {
                    podsInHallway.forEach { pod ->
                        if (openRooms[pod.typeId]) {
                            val range =
                                if (roomsX[pod.typeId] > pod.pos.x)
                                    pod.pos.x + 1..roomsX[pod.typeId]
                                else
                                    roomsX[pod.typeId] until pod.pos.x
                            if (range.none { x -> x to hallwayY in podsInHallway.map { it.pos } })
                                add(NewState(pods - pod + pod.copy(pos = emptyRoomPos[pod.typeId]!!)))
                        }
                    }
                }


                val inHallway = pods.filter { it.pos.y == hallwayY }.toSet()
                val inRooms = rooms.map { it.mapNotNull { p -> pods.singleOrNull { pod -> pod.pos == p } } }
                val occupied = pods.map(Amphipod::pos)
                val freePosInRoom: List<Point?> = layout.rooms.map { it.firstOrNull { p -> p !in occupied } }
                TODO()
            }
        }

        companion object {
            fun fromMap(map: Grid<Char>): NewState =
                NewState(map.area.allPoints().mapNotNull { p ->
                    map[p].takeIf { it.isLetter() }?.let { Amphipod(it, p) }
                }.toSet())
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as NewState

            if (pods != other.pods) return false

            return true
        }

        override fun hashCode(): Int {
            return pods.hashCode()
        }
    }

    val part1Layout = MapLayout.fromInput(initialMap)
    val part2Layout = MapLayout.fromInput(initialMap.take(3) +
            """
            |  #D#C#B#A#
            |  #D#B#A#C#
            """.trimMargin().toGrid() +
            initialMap.drop(3)
    )

    val initialState = State(listOf(
        initialMap.searchIndices { it == 'A' }.toList().sortedBy { it.y },
        initialMap.searchIndices { it == 'B' }.toList().sortedBy { it.y },
        initialMap.searchIndices { it == 'C' }.toList().sortedBy { it.y },
        initialMap.searchIndices { it == 'D' }.toList().sortedBy { it.y },
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

                    val firstFreeY = (sideRoomsY.last downTo sideRoomsY.first).firstOrNull { y ->
                        current.freeTo(destX to y)
                    }
                    if (firstFreeY != null)
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
    """.trimIndent(), 44169)
}

private fun String.toGrid(): Grid<Char> = split("\n").map { it.toList() }