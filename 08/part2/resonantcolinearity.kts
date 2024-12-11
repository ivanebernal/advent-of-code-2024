import java.io.File
typealias Coord = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]

var result = 0L
val nodes = mutableMapOf<Char, List<Coord>>()

val map = File(file).readLines()

for (i in 0 until map.size) {
    for (j in 0 until map[0].length) {
        val c = map[i][j]
        if (c != '.') {
            nodes[c] = nodes[c].orEmpty() + listOf(i to j)
        }
    }
}

val antiNodes = mutableSetOf<Coord>()

nodes.forEach { (signal, antennas) ->
    for (i in 0 until antennas.size) {
        for (j in (i + 1) until antennas.size) {
            val distance = antennas[i] - antennas[j]
            var antiNode1 = antennas[i]
            var antiNode2 = antennas[j]
            while (antiNode1.inRange()) {
                antiNodes.add(antiNode1)
                antiNode1 += distance
            }
            while (antiNode2.inRange())  {
                antiNodes.add(antiNode2)
                antiNode2 -= distance
            }
        }   
    }
}

println("Result: ${antiNodes.size}")


operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
fun Coord.inRange() = first in (0 until map.size) && second in (0 until map[0].length)