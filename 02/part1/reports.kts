import java.io.File
import java.util.PriorityQueue

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

var safeCount = 0

File(file).forEachLine { line ->
    var dir = 0
    val values = line.split(" ").mapNotNull { it.toIntOrNull() }
    var isSafe = true
    for (i in 1 until values.size) {
        val d = values[i] - values[i - 1]
        // unsafe
        if (d == 0) {
            isSafe = false
            break;
        }
        val nd = if (d > 0) 1 else -1
        if (dir == 0) {
            dir = nd
        } 
        
        if (nd != dir || Math.abs(d) > 3) {
            isSafe = false
            break;
        }
    }
    println("$line   $isSafe")
    if (isSafe) safeCount++
}

println("Safe reports: $safeCount")