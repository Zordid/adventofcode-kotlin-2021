package utils

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Test

internal class CombinatoricsTest {

    @Test
    fun `simple combinations of 2 elements`() {
        "a".combinations(2).toList().shouldBeEmpty()
        "ab".combinations(2).toList() shouldContainExactlyInAnyOrder
                listOf(
                    "ab"
                )
        "abc".combinations(2).toList() shouldContainExactlyInAnyOrder
                listOf(
                    "ab",
                    "ac",
                    "bc",
                )
        "abcd".combinations(2).toList() shouldContainExactlyInAnyOrder
                listOf(
                    "ab",
                    "ac",
                    "ad",
                    "bc",
                    "bd",
                    "cd",
                )
    }

    @Test
    fun `test permutations of elements`() {
        "".permutations().toList().shouldBeEmpty()
        "a".permutations().toList() shouldContainExactlyInAnyOrder
                listOf("a")
        "ab".permutations().toList() shouldContainExactlyInAnyOrder
                listOf("ab", "ba")
        "abc".permutations().toList() shouldContainExactlyInAnyOrder
                listOf(
                    "abc",
                    "acb",
                    "bac",
                    "cab",
                    "bca",
                    "cba"
                )
    }

}