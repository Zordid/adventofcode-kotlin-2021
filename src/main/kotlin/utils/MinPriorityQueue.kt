package utils

interface MinPriorityQueue<T> {
    fun isEmpty(): Boolean
    fun insert(element: T, priority: Int)
    fun remove(element: T)
    fun extractMin(): T
    fun peekOrNull(): T?
    fun decreasePriority(element: T, priority: Int)
}

class MinPriorityQueueImpl<T> : MinPriorityQueue<T> {
    private val elementToPrio = mutableMapOf<T, Int>()
    private val prioToElement = mutableMapOf<Int?, MutableSet<T>>()
    private val priorities = sortedSetOf<Int>()

    override fun isEmpty() = elementToPrio.isEmpty()

    fun contains(element: T) = elementToPrio.containsKey(element)

    override fun insert(element: T, priority: Int) {
        if (element in elementToPrio)
            remove(element)
        elementToPrio[element] = priority
        prioToElement.getOrPut(priority) { mutableSetOf() }.add(element)
        priorities.add(priority)
    }

    override fun remove(element: T) {
        val priority = getPriorityOf(element)
        prioToElement[priority]!!.remove(element)
        if (prioToElement[priority]!!.isEmpty()) {
            prioToElement.remove(priority)
            priorities.remove(priority)
        }
        elementToPrio.remove(element)
    }

    private fun getPriorityOf(element: T) = elementToPrio[element]

    override fun extractMin(): T {
        if (elementToPrio.isEmpty())
            throw NoSuchElementException()
        val result = prioToElement[priorities.first()]!!.first()
        remove(result)
        return result
    }

    override fun peekOrNull(): T? =
        prioToElement[priorities.firstOrNull()]?.first()

    override fun decreasePriority(element: T, priority: Int) {
        remove(element)
        insert(element, priority)
    }

}
