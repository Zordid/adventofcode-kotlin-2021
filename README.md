# Advent Of Code 2021
Solutions in [Kotlin](https://www.kotlinlang.org/), the most fun language on the planet!

My solutions to the ingenious [Advent Of Code 2021](https://adventofcode.com/)
by Eric Wastl.

This is my fifth year of Advent of Code coding in a row - my body gets trained again
to get up at 5:45 in the morning for almost a month... the addiction is real!

If you are into programming, logic, maybe also a little into competition, this one is for you as well!

### Overview of the puzzles
|Day |Title                             |Remarks
|---:|----------------------------------|----|
|  1 |Sonar Sweep                       |Let's get things started by some sliding windows!|

## My logbook of 2021

### Day 1: Sonar Sweep
Got up at 5:50 - knowing there will be just enough time to grab a coffee, but barely enough time to drink 
it while solving the warm-up-puzzle of 2021. Fingers crossed that the servers withstand the enormous
numbers of participants this year! 
The calendar worked - but the leaderboards are quite under stress. Gateway Timeouts and long waits...

As for the puzzle: take a list of depth measurements and do some comparison work on adjacent numbers.
Kotlin's standard library to the rescue: ```windowed(size, step)``` does the trick all by itself!