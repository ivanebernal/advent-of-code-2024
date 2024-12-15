import java.io.File

typealias Coord = Pair<Int, Int>
typealias Velocity = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val regex = "p=(-?\\d*),(-?\\d*) v=(-?\\d*),(-?\\d*)".toRegex()

val robots = mutableListOf<Coord>()

val hor = if (isTest) 11 else 101
val ver = if (isTest) 7 else 103
val time = 100

val quadrants = arrayOf(0, 0, 0, 0)

File(file).forEachLine { line ->
    val values = regex.matchEntire(line)!!.groupValues
    val p = values[1].toInt() to values[2].toInt()
    val v = values[3].toInt() to values[4].toInt()
    val distance = (v * time)
    val posRaw = p + distance
    val x = ((posRaw.first % hor) + hor) % hor
    val y = ((posRaw.second % ver) + ver) % ver

    // determine quadrant
    if (x > hor / 2 && y > ver / 2) quadrants[0]++
    if (x > hor / 2 && y < ver / 2) quadrants[1]++
    if (x < hor / 2 && y > ver / 2) quadrants[2]++
    if (x < hor / 2 && y < ver / 2) quadrants[3]++
}
println(quadrants.joinToString())
val result = quadrants.fold(1) { acc, q -> acc * q }

println("Result: $result")

operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
operator fun Coord.times(other: Int) = (this.first * other) to (this.second * other)
