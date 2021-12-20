package utils

interface MyGrid<T> {
    val area: Area
    fun getOrNull(p: Point): T?
    operator fun get(p: Point): T = getOrNull(p) ?: throw NoSuchElementException()
    fun getOrElse(p: Point, default: (Point) -> T): T = getOrNull(p) ?: default(p)

    companion object {
        fun <T> of(v: List<List<T>>): MyGrid<T> = BaseGrid(v)
        fun <T> of(v: Array<Array<T>>): MyGrid<T> = BaseArrayGrid(v)
        @JvmName("ofListOfString")
        fun of(v: List<String>): MyGrid<Char> = BaseCharGrid(v)
    }

    private class BaseGrid<T>(val underlying: List<List<T>>) : MyGrid<T> {
        override val area = underlying.area()
        override fun getOrNull(p: Point): T? = with(underlying) {
            if (p.y in indices && p.x in this[p.y].indices) this[p.y][p.x] else null
        }
    }

    private class BaseCharGrid(val underlying: List<String>) : MyGrid<Char> {
        override val area = underlying.area()
        override fun getOrNull(p: Point): Char? = with(underlying) {
            if (p.y in indices && p.x in this[p.y].indices) this[p.y][p.x] else null
        }
    }

    private class BaseArrayGrid<T>(val underlying: Array<Array<T>>) : MyGrid<T> {
        override val area = with(underlying) { origin to (first().size - 1 to size - 1) }
        override fun getOrNull(p: Point): T? = with(underlying) {
            if (p.y in indices && p.x in this[p.y].indices) this[p.y][p.x] else null
        }
    }
}