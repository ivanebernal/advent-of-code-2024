import java.io.File
import java.util.PriorityQueue
import java.util.ArrayDeque

typealias GridNode = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val maze = File(file).readLines()

var start = -1 to -1
var end = -1 to -1

maze.forEachIndexed { row, r ->
    r.forEachIndexed { col, c ->
        when (c) {
            'S' -> start = row to col
            'E' -> end = row to col
        }
    }
}

val directions = listOf(
        0 to 1,  // Right
        1 to 0, // Down
        0 to -1,  // Left
        -1 to 0,  // Up
    )

val path = aStarGrid(maze, start, end)

maze.forEachIndexed { row, r ->
    r.forEachIndexed { col, c ->
        if (path.contains(row to col)) print('O')
        else print(c)
    }
    println()
}

println(path.size)

fun aStarGrid(grid: List<String>, start: GridNode, goal: GridNode): Set<GridNode> {

    // Heuristic function: Manhattan distance
    fun heuristic(node1: GridNode, node2: GridNode): Int {
        return Math.abs(node1.first - node2.first) + Math.abs(node1.second - node2.second)
    }

    val rows = grid.size
    val cols = grid[0].length

    val openSet = PriorityQueue<Step>(compareBy { it.cost })
    openSet.add(Step(pos = start, dir = 0, cost = 0))

    val cameFrom = mutableMapOf<Pair<GridNode, Int>, MutableList<Pair<GridNode, Int>>>()
    val gScore = mutableMapOf<Pair<GridNode, Int>, Int>().withDefault { Int.MAX_VALUE }

    gScore[start to 0] = 0
    val path = mutableSetOf<GridNode>()
    var minCost = Int.MAX_VALUE
    
    while (openSet.isNotEmpty()) {
        val current = openSet.poll()

        if (current.pos == goal && current.cost <= minCost) {
            // Reconstruct path
            if (current.cost < minCost) {
                path.clear()
                minCost = current.cost
            }
            val q = ArrayDeque<Pair<GridNode, Int>>()
            q.add(current.pos to current.dir)
            while (!q.isEmpty()) {
                val (pos, dir) = q.removeFirst()
                path.add(pos)
                val prev = cameFrom[pos to dir]
                prev?.forEach { q.addFirst(it) }
            }
            path.add(start)
        }

        listOf(current.dir, current.dir.turnLeft(), current.dir.turnRight()).forEach { d ->
            val neighbor = current.pos + directions[d]
            // Check if the neighbor is within bounds and not a wall
            if (grid[neighbor] != '#') {
                val costOfMoving = if (current.dir == d) 1 else 1001
                val currentScore = gScore.getValue(current.pos to current.dir)
                val tentativeGScore = if (currentScore == Int.MAX_VALUE) Int.MAX_VALUE else currentScore + costOfMoving
                val nextStep = Step(
                        pos = neighbor, 
                        dir = d,
                        cost = tentativeGScore + heuristic(neighbor, goal)
                    )
                if (tentativeGScore < gScore.getValue(neighbor to d)) {
                    cameFrom[neighbor to d] = mutableListOf(current.pos to current.dir)
                    gScore[neighbor to d] = tentativeGScore
                    openSet.add(nextStep)
                } else if (tentativeGScore == gScore.getValue(neighbor to d)) {
                    cameFrom.getOrPut(neighbor to d) { mutableListOf() }.add(Pair(current.pos, current.dir))
                }
            }
        }
    }

    return path
}

fun Int.step() = directions[this]
fun Int.turnRight() = (this + 1) % directions.size
fun Int.turnLeft() = (this - 1 + directions.size) % directions.size
operator fun GridNode.plus(other: GridNode) = (this.first + other.first) to (this.second + other.second)
operator fun List<String>.get(n: GridNode) = this[n.first][n.second]

data class Step(
    val pos: GridNode,
    val dir: Int,
    val cost: Int
)
