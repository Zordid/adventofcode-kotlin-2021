import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DaysTest {

    @Test
    fun `day 16`() {
        Day16().check(938, 1495959086337)
    }

    companion object {
        fun Day.check(expectedPart1: Any?, expectedPart2: Any?) =
            assertSoftly(this) {
                if (expectedPart1 != null) withClue("Part 1") {
                    part1.toString() shouldBe expectedPart1.toString()
                }
                if (expectedPart2 != null) withClue("Part 2") {
                    part2.toString() shouldBe expectedPart2.toString()
                }
            }
    }

}