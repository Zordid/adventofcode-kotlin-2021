package utils

/**
 * Generates all combinations of the elements of the given list for the requested size.
 * Note: combinations do not include all their permutations!
 * @receiver the collection to take elements from
 * @param size the size of the combinations to create
 * @return a sequence of all combinations
 */
fun <T> Collection<T>.combinations(size: Int): Sequence<List<T>> =
    toList().combinations(size)

/**
 * Generates all combinations of the elements of the given list for the requested size.
 * Note: combinations do not include all their permutations!
 * @receiver the list to take elements from
 * @param size the size of the combinations to create
 * @return a sequence of all combinations
 */
fun <T> List<T>.combinations(size: Int): Sequence<List<T>> =
    when (size) {
        0 -> emptySequence()
        1 -> asSequence().map { listOf(it) }
        else -> sequence {
            this@combinations.forEachIndexed { index, element ->
                this@combinations.subList(index + 1, this@combinations.size).combinations(size - 1).forEach {
                    yield(it.copyAndInsert(0, element))
                }
            }
        }
    }

/**
 * Generates a sequence of all permutations of the given list of elements.
 * @receiver the list of elements for permutation of order
 * @return a sequence of all permutations of the given list
 */
fun <T> Collection<T>.permutations(): Sequence<List<T>> =
    when (size) {
        0 -> emptySequence()
        1 -> sequenceOf(toList())
        else -> {
            val head = first()
            val tail = drop(1)
            tail.permutations().flatMap { perm ->
                (0..perm.size).asSequence().map { perm.copyAndInsert(it, head) }
            }
        }
    }

fun String.permutations(): Sequence<String> =
    toList().permutations().map { it.joinToString("") }

fun String.combinations(size: Int): Sequence<String> =
    toList().combinations(size).map { it.joinToString("") }

private fun <T> List<T>.copyAndInsert(insertAt: Int, element: T): List<T> =
    List(size + 1) { idx ->
        when {
            idx < insertAt -> this[idx]
            idx == insertAt -> element
            else -> this[idx - 1]
        }
    }