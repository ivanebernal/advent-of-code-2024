import java.io.File
import java.util.ArrayDeque
typealias Coord = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val garden = File(file).readLines()
val m = garden.size
val n = garden[0].length

val visited = mutableSetOf<Coord>()

val areas = mutableMapOf<Char, Int>()
val perimeters = mutableMapOf<Char, Int>()

val steps = listOf(
    0 to 1,
    1 to 0,
    0 to -1,
    -1 to 0
)
var result = 0

for (row in 0 until m) {
    for (col in 0 until n) {
        if (!visited.contains(row to col)) {
            result += findAreaPrice(row to col)
        }
    }
}

println("Result: $result")

fun findAreaPrice(start: Coord): Int {
    val queue = ArrayDeque<Coord>()
    queue.addFirst(start)
    var area = 0
    var perimeter = 0
    while (!queue.isEmpty()) {
        val region = queue.removeFirst()
        if (visited.contains(region)) continue
        visited.add(region)
        val id = garden[region]
        area++
        for (step in steps) {
            val next = region + step
            if (!next.inRange() || garden[next] != id) {
                perimeter++
            } else {
                queue.add(next)
            }
        }
    }
    return area * perimeter
}


operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
operator fun List<String>.get(coord: Coord) = this[coord.first][coord.second]
fun Coord.inRange() = first in (0 until m) && second in (0 until n)