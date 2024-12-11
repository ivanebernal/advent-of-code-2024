import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""
val isTest = args.contains("-t")

val dirs = listOf('^', '>', 'v', '<')
fun List<Char>.next(curr: Char) = this[(indexOf(curr) + 1) % size]
fun List<Char>.prev(curr: Char) = this[(indexOf(curr) + 3) % size]
val steps = mapOf(
    '^' to (-1 to 0),
    '>' to (0 to 1),
    'v' to (1 to 0),
    '<' to (0 to -1),
)

val map = File(file).readLines()
val initialPos = findGuard(map)
var currPos = initialPos
var currDir = '^'
var visited = mutableListOf<Pair<Int, Int>>()
while(currPos.inRange()) {
    if (hasObstacle()) {
        currDir = dirs.next(currDir)
    } else {
        visited.add(currPos)
        currPos = currPos + steps[currDir]!!
    }
}
val validObstacles = mutableSetOf<Pair<Int, Int>>()
for (pos in visited) {
    if (pos == initialPos) continue
    val obstacle = pos
    if (!obstacle.inRange() || map[obstacle] == '#') continue
    if (hasLoop(obstacle)) {
        validObstacles.add(obstacle)
    }
}

fun hasLoop(obstacle: Pair<Int, Int>): Boolean {
    var pos = initialPos
    var dir = '^'
    val visited = mutableSetOf<Pair<Pair<Int, Int>, Char>>()
    while(pos.inRange()) {
        if (visited.contains(pos to dir)) return true
        visited.add(pos to dir)
        if (hasObstacle(pos, dir, obstacle)) {
            dir = dirs.next(dir)
        } else {
            pos = pos + steps[dir]!!
        }
        if(isTest) animateMap(pos, dir, obstacle)
    }
    return false
}


println("Result: ${validObstacles.size}")
// if(isTest) {
//     validObstacles.forEach { obstacle ->
//         printMap(obstacle)
//         println()
//     }
// }

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


fun hasObstacle(pos: Pair<Int, Int>, dir: Char, obstacle: Pair<Int, Int>): Boolean {
    val nextDir = pos + steps[dir]!!
    return nextDir.inRange() && (map[nextDir] == '#' || nextDir == obstacle)
}

fun animateMap(pos: Pair<Int, Int>, dir: Char, obstacle: Pair<Int, Int>) {
    print("\u001b[H\u001b[2J")
    for (i in 0 until map.size) {
        for (j in 0 until map[0].length) {
            if (i to j == pos) {
                print(dir)
            } else if (i to j == obstacle) {
                print('O')                
            } else {
                print(map[i][j])
            }
        }
        println()
    }
    Thread.sleep(50)
}

fun printMap(obstacle: Pair<Int, Int>) {
    for (i in 0 until map.size) {
        for (j in 0 until map[0].length) {
            if (i to j == obstacle) {
                print('O')                
            } else {
                print(map[i][j])
            }
        }
        println()
    }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (first + other.first) to (second + other.second)
fun Pair<Int, Int>.inRange() = first in (0 until map.size) && second in (0 until map[0].length)
operator fun List<String>.get(pair: Pair<Int, Int>) = this[pair.first][pair.second]