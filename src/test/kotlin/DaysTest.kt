import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class DaysTest {

    @TestFactory
    fun all(): List<DynamicTest> = tests(
        test<Day01>(1298, 1248),
        test<Day02>(1580000, 1251263225),
        test<Day03>(1458194, 2829354),
        test<Day04>(28082, 8224),
        test<Day05>(6283, 18864),
        test<Day06>(372300, 1675781200288),
        test<Day07>(337488, 89647695),
        test<Day08>(239, 946346),
        test<Day09>(512, 1600104),
        test<Day10>(344193, 3241238967),
        test<Day11>(1747, 505),
        test<Day12>(5212, 134862),
        test<Day13>(592, DAY13PT2),
        test<Day14>(3555, 4439442043739),
        test<Day15>(755, 3016),
        test<Day16>(938, 1495959086337),
    )

    companion object {
        val DAY13PT2 = """
            |  ##  ##   ##    ## #### #### #  # #  #
            |   # #  # #  #    # #    #    # #  #  #
            |   # #    #  #    # ###  ###  ##   #  #
            |   # # ## ####    # #    #    # #  #  #
            |#  # #  # #  # #  # #    #    # #  #  #
            | ##   ### #  #  ##  #### #    #  #  ## 
        """.trimMargin()
    }
}

private fun tests(vararg d: List<DynamicTest>) = d.asList().flatten().also { verbose = false }

private inline fun <reified D : Day> test(expectedPart1: Any? = null, expectedPart2: Any? = null) = listOfNotNull(
    expectedPart1?.let {
        DynamicTest.dynamicTest("${D::class.simpleName} - Part 1")
        { create<D>().part1.toString() shouldBe expectedPart1.toString() }
    },
    expectedPart2?.let {
        DynamicTest.dynamicTest("${D::class.simpleName} - Part 2")
        { create<D>().part2.toString() shouldBe expectedPart2.toString() }
    })
