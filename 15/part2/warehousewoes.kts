import java.io.File
import java.util.ArrayDeque

typealias Coord = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

var walls = mutableSetOf<Coord>()
var boxes = mutableSetOf<Coord>()
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

robot = robot.first to (robot.second * 2)
walls = walls.map { it.first to (it.second * 2) }.toMutableSet()
// walls = walls + walls.map { (it.first + 1) to it.second }.toSet()
boxes = boxes.map { it.first to (it.second * 2) }.toMutableSet()

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
}
if (isTest) drawStep()

// returns true if the boxes are moved
fun moveBoxes(robot: Coord, step: Coord): Boolean {
    return if (isVertical(step)) moveVertical(robot, step) else moveHorizontal(robot, step)
}

fun moveHorizontal(robot: Coord, step: Coord): Boolean {
    var pos = robot + step
    // find out what's after the boxes
    while ((!walls.contains(pos) || !walls.contains(pos.prevSpace())) && (boxes.contains(pos) || boxes.contains(pos.prevSpace()))) {
        pos += step
    }
    // if there's a wall we don't move
    if (walls.contains(pos) || walls.contains(pos.prevSpace())) return false
    // if there's a space we move all the boxes
    while (pos != robot) {
        pos -= step
        if (boxes.contains(pos)) {
            boxes.remove(pos)
            boxes.add(pos + step)
        }
    }
    return true
}

fun moveVertical(robot: Coord, step: Coord): Boolean {
    val queue = ArrayDeque<Coord>()
    var pos = robot + step
    val boxesToMove = mutableSetOf<Coord>()
    if (boxes.contains(pos)) {
        queue.addFirst(pos)
        queue.addFirst(pos.nextSpace())
    } else if (boxes.contains(pos.prevSpace())) {
        queue.addFirst(pos)
        queue.addFirst(pos.prevSpace())
    } else if (walls.contains(pos) || walls.contains(pos.prevSpace())) {
        return false
    }
    while(!queue.isEmpty()) {
        val space = queue.removeFirst()
        if (boxes.contains(space)) boxesToMove.add(space)
        pos = space + step
        if (walls.contains(pos) || walls.contains(pos.prevSpace())) return false
        if (boxes.contains(pos)) {
            queue.add(pos)
            queue.add(pos.nextSpace())
        } else if (boxes.contains(pos.prevSpace())) {
            queue.add(pos)
            queue.add(pos.prevSpace())
        }
    }
    boxes.removeAll(boxesToMove)
    boxes.addAll(boxesToMove.map { it + step })
    return true
}

fun isVertical(step: Coord) = step == steps['v'] || step == steps['^']

fun drawStep() {
    for (row in 0 until map.size) {
        for (col in 0 until map[0].length * 2) {
            when {
                robot == row to col -> print('@')
                walls.contains(row to col) || walls.contains(row to col - 1) -> print("#")
                boxes.contains(row to col) -> print("[")
                boxes.contains(row to col - 1) -> print("]")
                else -> print(".")
            }
        }
        println()
    }
    println()
}

// calculate result
val result = boxes.fold(0) { acc, box -> acc + box.first * 100 + box.second } 

println("Result: $result")


operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
operator fun Coord.get(other: Int) = (this.first * other) to (this.second * other)
fun Coord.prevSpace() = this.first to (this.second - 1)
fun Coord.nextSpace() = this.first to (this.second + 1)