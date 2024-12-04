import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

val puzzle = File(file).readLines()
val n = puzzle.size

var result = 0

for (i in 0 until n) {
    for (j in 0 until n) {
        if (hasXmas(i to j)) result++
    }
}

println("Result: $result")

fun hasXmas(start: Pair<Int, Int>): Boolean {
    val (r, c) = start
    if (r + 2 >= n || c + 2 >= n) return false
    val d1 = listOf(puzzle[r][c], puzzle[r + 1][c + 1], puzzle[r + 2][c + 2]).joinToString("")
    val d2 = listOf(puzzle[r][c + 2], puzzle[r + 1][c + 1], puzzle[r + 2][c]).joinToString("")
    return d1.isSamOrMas() && d2.isSamOrMas()
}

fun String.isSamOrMas() = this == "SAM" || this == "MAS"