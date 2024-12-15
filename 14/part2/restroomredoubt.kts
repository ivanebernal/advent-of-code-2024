import java.io.File

typealias Coord = Pair<Int, Int>
typealias Velocity = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val regex = "p=(-?\\d*),(-?\\d*) v=(-?\\d*),(-?\\d*)".toRegex()

val hor = if (isTest) 11 else 101
val ver = if (isTest) 7 else 103
val time = 100

val robots = mutableListOf<Robot>()
val positions = mutableSetOf<Coord>()
var seconds = 0

File(file).forEachLine { line ->
    val values = regex.matchEntire(line)!!.groupValues
    val p = values[1].toInt() to values[2].toInt()
    val v = values[3].toInt() to values[4].toInt()
    robots.add(Robot(p, v))
}

var quadrants = arrayOf(0, 0, 0, 0)
tickSeconds(13)

while (true) {
    quadrants = arrayOf(0, 0, 0, 0)
    drawFrame()
    tickSeconds(101)
    readln()
}

fun tickSeconds(seconds: Int) {
    positions.clear()
    for (i in 0 until robots.size) {
        val robot = robots[i]
        val posRaw = robot.pos + (robot.vel * seconds)
        val x = ((posRaw.first % hor) + hor) % hor
        val y = ((posRaw.second % ver) + ver) % ver
        robots[i] = robots[i].copy(pos = x to y)
        if (x < hor / 2 && y < ver / 2) quadrants[0]++
        if (x > hor / 2 && y < ver / 2) quadrants[1]++
        if (x < hor / 2 && y > ver / 2) quadrants[2]++
        if (x > hor / 2 && y > ver / 2) quadrants[3]++
        positions.add(x to y)
    }
    this.seconds += seconds
}

fun drawFrame() {
    print("\u001b[H\u001b[2J")
    for (row in 0 until ver) {
        for (col in 0 until hor) {
            if (positions.contains(col to row)) print("*") else print(" ")
        }
        println()
    }
    println("SECONDS: $seconds")
}

fun isSymetric(): Boolean {
    return positions.all { (x, y) -> 
        val mirror = hor - x - 1 to y
        positions.contains(mirror)
    }
}

operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
operator fun Coord.times(other: Int) = (this.first * other) to (this.second * other)

data class Robot(
    val pos: Coord,
    val vel: Velocity
)