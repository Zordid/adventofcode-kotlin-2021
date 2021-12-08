package utils

/**
 * Generates all combinations of the elements of the given list for the requested size.
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
                    yield(it.toMutableList().apply { add(0, element) })
                }
            }
        }
    }

/**
 * Generates a sequence of all permutations of the given list of elements.
 * @receiver the list of elements for permutation of order
 * @return a sequence of all permutations of the given list
 */
fun <T> List<T>.permutations(): Sequence<List<T>> =
    when (size) {
        0 -> emptySequence()
        1 -> sequenceOf(this)
        else -> {
            val first = first()
            val remaining = subList(1, size)
            sequence {
                remaining.permutations().forEach { perm ->
                    for (i in 0..perm.size) {
                        yield(perm.toMutableList().apply { add(i, first) })
                    }
                }
            }
        }
    }

fun String.permutations(): Sequence<String> =
    toList().permutations().map { it.joinToString("") }

fun String.combinations(size: Int): Sequence<String> =
    toList().combinations(size).map { it.joinToString("") }