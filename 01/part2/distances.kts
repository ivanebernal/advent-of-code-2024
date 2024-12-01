import java.io.File
import java.util.PriorityQueue

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""


val numberToFreq = mutableMapOf<Int, Int>()
val left = mutableListOf<Int>()

File(file).forEachLine { line ->
    val values = line.split("   ").mapNotNull { it.toIntOrNull() }
    left.add(values[0])
    numberToFreq[values[1]!!] = (numberToFreq[values[1]!!] ?: 0) + 1
}

var result = 0

left.forEach { num ->
    result += num * (numberToFreq[num] ?: 0)
}

println("Similarity score: $result")