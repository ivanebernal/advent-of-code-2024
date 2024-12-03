import java.io.File
import java.util.PriorityQueue

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

var safeCount = 0

File(file).forEachLine { line ->
    val values = line.split(" ").mapNotNull { it.toIntOrNull() }
    val isSafe = isSafe(values, true)
    if (isSafe) safeCount++
    println("$line $isSafe")
}

fun isSafe(report: List<Int>, canRemoveLevel: Boolean): Boolean {

    fun isSafeRemovingLevel(): Boolean {
        for (i in 0 until report.size) {
            val newReport = report.toMutableList().apply { removeAt(i) }
            if (isSafe(newReport, false)) return true
        }
        return false
    }

    var dir = 0
    for (i in 1 until report.size) {
        val d = report[i] - report[i - 1]
        // unsafe
        if (d == 0) {
            return canRemoveLevel && isSafeRemovingLevel()
        }
        val nd = if (d > 0) 1 else -1
        if (dir == 0) dir = nd
        if (nd != dir || Math.abs(d) > 3) {
            return canRemoveLevel && isSafeRemovingLevel()
        }
    }
    return true
}

println("Safe reports: $safeCount")