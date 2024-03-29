@file:Suppress("unused")

package utils

import java.util.*

interface Graph<N> {
    fun neighborsOf(node: N): Collection<N>

    fun cost(from: N, to: N): Int = throw NotImplementedError("needs a cost fun")
    fun costEstimation(from: N, to: N): Int = throw NotImplementedError("needs a costEstimation fun")
}

fun <N> Graph<N>.breadthFirstSearch(start: N, destinationPredicate: (N) -> Boolean): Stack<N> =
    breadthFirstSearch(start, ::neighborsOf, destinationPredicate)

fun <N> Graph<N>.breadthFirstSearch(start: N, destination: N): Stack<N> =
    breadthFirstSearch(start, ::neighborsOf) { it == destination }

fun <N> Graph<N>.depthFirstSearch(start: N, destinationPredicate: (N) -> Boolean): Stack<N> =
    depthFirstSearch(start, ::neighborsOf, destinationPredicate)

fun <N> Graph<N>.depthFirstSearch(start: N, destination: N): Stack<N> =
    depthFirstSearch(start, ::neighborsOf) { it == destination }

fun <N> Graph<N>.completeAcyclicTraverse(start: N) =
    SearchEngineWithNodes(::neighborsOf).completeAcyclicTraverse(start)

fun <N> Graph<N>.aStarSearch(
    start: N,
    destination: N,
    c: (N, N) -> Int = this::cost,
    cEstimation: (N, N) -> Int = this::costEstimation,
) =
    AStar(start, ::neighborsOf, c, cEstimation).search(destination)

fun <N> Graph<N>.dijkstraSearch(start: N, destination: N) =
    Dijkstra(::neighborsOf, ::cost).search(start) { it == destination }

fun <N> Graph<N>.dijkstraSearch(start: N, destinationPredicate: (N) -> Boolean) =
    Dijkstra(::neighborsOf, ::cost).search(start, destinationPredicate)