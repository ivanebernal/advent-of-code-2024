import java.io.File
import java.util.ArrayDeque
typealias Coord = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val map = File(file).readLines()
val m = map.size
val n = map[0].length
var result = 0
val steps = listOf(
    0 to 1,
    1 to 0,
    0 to -1,
    -1 to 0
)

for (row in 0 until m) {
    for (col in 0 until n) {
        val coord = row to col
        if (map[coord] == '0') {
            result += findTrailScore(coord)
        }
    }
}

println("Result: $result")

fun findTrailScore(coord: Coord): Int {
    var res = 0
    val queue = ArrayDeque<Coord>()
    queue.addFirst(coord)
    while (!queue.isEmpty()) {
        val node = queue.removeFirst()
        if (map[node] == '9') {
            res++
            continue
        }
        for (step in steps) {
            val nextNode = node + step
            if (!nextNode.inRange()) continue
            if (map[nextNode] - map[node] == 1) queue.addFirst(nextNode)
        }
    }
    return res
}

operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
operator fun List<String>.get(coord: Coord) = map[coord.first][coord.second]
fun Coord.inRange() = first in (0 until m) && second in (0 until n)