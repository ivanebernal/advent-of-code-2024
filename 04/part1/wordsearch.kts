import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

val puzzle = File(file).readLines()
val n = puzzle.size

var result = 0

val xmas = "XMAS"
val samx = "SAMX"

// horizontal
for (line in puzzle) result += countXmas(line)
// vertical
for (i in 0 until n) {
    val line = puzzle.map { it[i] }.joinToString("")
    result += countXmas(line)
}
// diagonal
for (i in 0 until n) {
    result += (0 .. i).map { puzzle[it][i - it] }.joinToString("").let { countXmas(it) } // */
    result += (0 .. i).map { puzzle[n - 1 - i + it][it] }.joinToString("").let { countXmas(it) } // *\
}

for (i in 0 until n - 1) {
    result += (0 .. i).map { puzzle[it][n - 1 - i + it] }.joinToString("").let { countXmas(it) }// \*
    result += (0 .. i).map { puzzle[n - 1 - i + it][n - 1 - it] }.joinToString("").let { countXmas(it) } // /*
}
println("Result: $result")

fun countXmas(line: String): Int {
    var startIndex = line.indexOf(xmas)
    var count = 0
    while (startIndex >= 0) {
        count++
        startIndex = line.indexOf(xmas, startIndex + 1)
    }
    startIndex = line.indexOf(samx)
    while (startIndex >= 0) {
        count++
        startIndex = line.indexOf(samx, startIndex + 1)
    }
    return count
}