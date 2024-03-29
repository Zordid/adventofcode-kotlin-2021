import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class DaysTest {

    @TestFactory
    fun `AoC 2021`() = aocTests {
        test<Day01>(1298, 1248)
        test<Day02>(1580000, 1251263225)
        test<Day03>(1458194, 2829354)
        test<Day04>(28082, 8224)
        test<Day05>(6283, 18864)
        test<Day06>(372300, 1675781200288)
        test<Day07>(337488, 89647695)
        test<Day08>(239, 946346)
        test<Day09>(512, 1600104)
        test<Day10>(344193, 3241238967)
        test<Day11>(1747, 505)
        test<Day12>(5212, 134862)
        test<Day13>(
            592, """
                    |  ##  ##   ##    ## #### #### #  # #  #
                    |   # #  # #  #    # #    #    # #  #  #
                    |   # #    #  #    # ###  ###  ##   #  #
                    |   # # ## ####    # #    #    # #  #  #
                    |#  # #  # #  # #  # #    #    # #  #  #
                    | ##   ### #  #  ##  #### #    #  #  ## 
                """.trimMargin()
        )
        test<Day14>(3555, 4439442043739)
        test<Day15>(755, 3016)
        test<Day16>(938, 1495959086337)
        test<Day17>(11175, 3540)
        test<Day18>(4176, 4633)
        test<Day19>(419, 13210)
        test<Day20>(5291, 16665)
        test<Day21>(713328, 92399285032143)
        test<Day22>(546724, 1346544039176841)
    }

}

private fun aocTests(builder: AoCTestBuilder.() -> Unit): List<DynamicTest> =
    AoCTestBuilder().apply(builder).build().also { verbose = false }

private class AoCTestBuilder {

    private val tests = mutableListOf<DynamicTest>()

    inline fun <reified D : Day> test(expectedPart1: Any? = null, expectedPart2: Any? = null) {
        tests += listOfNotNull(
            expectedPart1?.let {
                DynamicTest.dynamicTest("${D::class.simpleName} - Part 1")
                { create<D>().part1.toString() shouldBe expectedPart1.toString() }
            },
            expectedPart2?.let {
                DynamicTest.dynamicTest("${D::class.simpleName} - Part 2")
                { create<D>().part2.toString() shouldBe expectedPart2.toString() }
            })
    }

    fun build(): List<DynamicTest> = tests

}

