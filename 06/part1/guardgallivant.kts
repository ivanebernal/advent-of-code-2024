import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

val dirs = listOf('^', '>', 'v', '<')
fun List<Char>.next(curr: Char) = this[(indexOf(curr) + 1) % size]
val steps = mapOf(
    '^' to (-1 to 0),
    '>' to (0 to 1),
    'v' to (1 to 0),
    '<' to (0 to -1),
)

val map = File(file).readLines()
var currPos = findGuard(map)
var currDir = '^'
var path = mutableSetOf<Pair<Int, Int>>()
while(currPos.inRange()) {
    if (hasObstacle()) {
        currDir = dirs.next(currDir)
    } else {
        path.add(currPos)
        currPos = currPos + steps[currDir]!!
    }
}

println("Result: ${path.size}")

fun findGuard(map: List<String>): Pair<Int, Int> {
    for (i in 0 until map.size) {
        for (j in 0 until map[i].length) {
            if (map[i][j] == '^') return i to j
        }
    }
    throw Exception("Guard not found")
}

fun hasObstacle(): Boolean {
    val nextDir = currPos + steps[currDir]!!
    return nextDir.inRange() && map[nextDir] == '#'
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (first + other.first) to (second + other.second)
fun Pair<Int, Int>.inRange() = first in (0 until map.size) && second in (0 until map[0].length)
operator fun List<String>.get(pair: Pair<Int, Int>) = this[pair.first][pair.second]