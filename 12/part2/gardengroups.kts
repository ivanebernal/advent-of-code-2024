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
    val edges = mutableSetOf<Coord>()
    queue.addFirst(start)
    var area = mutableSetOf<Coord>()
    var sides = 0
    while (!queue.isEmpty()) {
        val region = queue.removeFirst()
        if (visited.contains(region)) continue
        visited.add(region)
        val id = garden[region]
        area.add(region)
        for (step in steps) {
            val next = region + step
            if (next.isOutOfRegion(id)) {
                edges.add(region)
            } else {
                queue.add(next)
            }
        }
        // calculate sides for internal corners
        val adj = steps.map { region + it }
        var borders = 0
        for (i in 0 until 4) {
            if (adj[i].isOutOfRegion(id) && adj[(i + 1) % 4].isOutOfRegion(id)) {
                borders++
            }
        }
        val e = when (borders) {
            1 -> 1
            2 -> 2
            3, 4 -> 4
            else -> 0
        }
        sides += borders
    }
    sides += calculateExternalCorners(area, edges)
    println("Region: ${garden[start]}. Area: ${area.size}. Sides: $sides")
    return area.size * sides
}

fun calculateExternalCorners(area: Set<Coord>, edges: Set<Coord>): Int {
    val visited = mutableSetOf<Coord>()
    val diagonals = listOf(
        1 to 1,
        -1 to -1,
        1 to -1,
        -1 to 1
    )
    var result = 0
    for (edge in edges) {
        for (diagonal in diagonals) {
            val d = diagonal + edge
            val innerCorner1 = (diagonal.first to 0) + edge
            val innerCorner2 = (0 to diagonal.second) + edge
            if (visited.contains(d)) continue
            if (edges.contains(d) && 
                (area.contains(innerCorner1) && !area.contains(innerCorner2) || 
                area.contains(innerCorner2) && !area.contains(innerCorner1))
            ) result++
        }
        visited.add(edge)
    }
    return result
}


operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
operator fun List<String>.get(coord: Coord) = this[coord.first][coord.second]
fun Coord.inRange() = first in (0 until m) && second in (0 until n)
fun Coord.isOutOfRegion(region: Char) = !this.inRange() || garden[this] != region