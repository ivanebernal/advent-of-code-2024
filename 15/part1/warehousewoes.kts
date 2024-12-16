import java.io.File

typealias Coord = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val walls = mutableSetOf<Coord>()
val boxes = mutableSetOf<Coord>()
var robot = -1 to -1

val input = File(file).readLines()
val map = input.subList(0, input.indexOf(""))
val moves = input.subList(input.indexOf("") + 1, input.size).joinToString("")

// find boxes, walls, and robot
for (row in 0 until map.size) {
    for (col in 0 until map[0].length) {
        when (map[row][col]) {
            '@' -> robot = row to col
            '#' -> walls.add(row to col)
            'O' -> boxes.add(row to col)
        }
    }
}

val steps = mapOf(
    '^' to (-1 to 0),
    '>' to (0 to 1),
    '<' to (0 to -1),
    'v' to (1 to 0),
)

// move
moves.forEach { move ->
    val step = steps[move]!!
    if (moveBoxes(robot, step)) robot += step
    // drawStep()
}

// returns true if the boxes are moved
fun moveBoxes(robot: Coord, step: Coord): Boolean {
    var pos = robot + step
    // find out what's after the boxes
    while (!walls.contains(pos) && boxes.contains(pos)) {
        pos += step
    }
    // if there's a wall we don't move
    if (walls.contains(pos)) return false
    // if there's a space we move all the boxes
    while (pos != robot + step) {
        pos -= step
        boxes.remove(pos)
        boxes.add(pos + step)
    }
    return true
}

fun drawStep() {
    for (row in 0 until map.size) {
        for (col in 0 until map[0].length) {
            when {
                robot == row to col -> print('@')
                walls.contains(row to col) -> print('#')
                boxes.contains(row to col) -> print('O')
                else -> print('.')
            }
        }
        println()
    }
    println()
}

// calculate result
val result = boxes.fold(0) { acc, box -> acc + (100 * box.first) + box.second } 

println("Result: $result")


operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
operator fun Coord.get(other: Int) = (this.first * other) to (this.second * other)