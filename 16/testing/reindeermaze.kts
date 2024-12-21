import java.io.File
import java.util.PriorityQueue

typealias Coord = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val maze = File(file).readLines()

val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
var start = -1 to -1
var end = -1 to -1
val walls = mutableSetOf<Coord>()
val queue = PriorityQueue<Step>(compareBy<Step> { it.cost })

maze.forEachIndexed { row, r ->
    r.forEachIndexed { col, c ->
        when (c) {
            '#' -> walls.add(row to col)
            'S' -> {
                start = row to col
                queue.add(Step(pos = start, dir = 0, cost = 0))
            }
            'E' -> {
                end = row to col
                // queue.add(Step(pos = end, dir = 0, cost = Int.MAX_VALUE))
            }
            // else -> queue.add(Step(pos = row to col, dir = 0, cost = Int.MAX_VALUE))
        }
    }
}

val distances = mutableMapOf<Coord, Int>()
val prev = mutableMapOf<Coord, Coord>()
distances[start] = 0
while(!queue.isEmpty()) {
    val n = queue.poll()!!
    val dist = distances[n.pos]
    listOf(n.dir.turnLeft(), n.dir, n.dir.turnRight()).forEach { d ->
        val next = n.pos + d.step()
        if (!walls.contains(next)) {
            val cost = n.cost + if (d == n.dir) 1 else 1001
            if (cost <= (distances[next] ?: Int.MAX_VALUE)) {
                distances[next] = cost
                prev[next] = n.pos
                queue.add(Step(pos = next, dir = d, cost = cost))
            }
        }
    }
}

println(distances[end])

fun Int.step() = directions[this]
fun Int.turnRight() = (this + 1) % directions.size
fun Int.turnLeft() = (this - 1 + directions.size) % directions.size
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)

data class Step(
    val pos: Coord,
    val dir: Int,
    val cost: Int
)