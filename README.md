# Advent Of Code 2021
Solutions in [Kotlin](https://www.kotlinlang.org/), the most fun language on the planet!

My solutions to the ingenious [Advent Of Code 2021](https://adventofcode.com/)
by Eric Wastl.

This is my fifth year of Advent of Code coding in a row - my body gets trained again
to get up at 5:45 in the morning for almost a month... the addiction is real!

If you are into programming, logic, maybe also a little into competition, this one is for you as well!

### Overview of the puzzles
|Day |Title                             |Runtime|Remarks|
|---:|----------------------------------|---:|----|
|  1 |Sonar Sweep                       |    |Let's get things started by some sliding windows!|
|  2 |Dive!                             |    |Figure out a submarines course from numbers      |
|  3 |Binary Diagnostic                 |    |Some bit magic that can drive you nuts           |
| 14 |Extended Polymerization           |2ms |It's all about recursion plus caching...|

## My logbook of 2021

### Day 14: Extended Polymerization
What a cool hack! In the beginning, the straight forward processing approach proved correct and
feasible for part 1. 10 iterations of polymerization are possible without further thought.
But for 40 iterations, the world is a different one. Took me about half an hour to crack this - 
shame on me - to have a hacky, but feasible 2-step approach, where I did 20 iterations of the complete
string and with this intermediate result, I focused on a cached-what-happens-to-each-pair-approach.
So, windowing over the intermediate and asking "what happens to this pair, what happens to the next pair"
while keeping the results in a cache map.
Worked. 17 seconds!

But after I got my 2 stars, no, that's not what I am happy with. So, back to the drawing board.
The result is amazingly simple. For each pair and the resulting statistics (how many chars?) the
recursively working fun needs a cache wrapper using getOrDefault around it. Done.
Optimizing the data structure to hold the counts into a simple LongArray (of all capital chars 'A' 
through 'Z' and their respective counts) leads to a 2ms second solution for part 2. I'm satisfied with
that.

### Day 3: Binary Diagnostic
What a hacky start for a relatively easy task. This turned out to be a challenge to 
read carefully. Trying to get a few operations and counts under control
made me program in good old copy&paste style with 50+ lines and very dirty String stuff.
Even the cleanup does not feel satisfying in the end...

### Day 2: Dive!
Reading a puzzle before coding is always a good idea! Lost it all when accepting negative 
numbers for depths despite the fact that this was explicitly NOT the case. READ! *sigh*

### Day 1: Sonar Sweep
Got up at 5:50 - knowing there will be just enough time to grab a coffee, but barely enough time to drink 
it while solving the warm-up-puzzle of 2021. Fingers crossed that the servers withstand the enormous
numbers of participants this year! 
The calendar worked - but the leaderboards are quite under stress. Gateway Timeouts and long waits...

As for the puzzle: take a list of depth measurements and do some comparison work on adjacent numbers.
Kotlin's standard library to the rescue: ```windowed(size, step)``` does the trick all by itself!
