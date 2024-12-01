import java.io.File
import java.util.PriorityQueue

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""


val left = PriorityQueue<Int>()
val right = PriorityQueue<Int>()

File(file).forEachLine { line ->
    val values = line.split("   ").mapNotNull { it.toIntOrNull() }
    left.add(values[0])
    right.add(values[1])
}

var result = 0

while (!left.isEmpty()) {
    result += Math.abs(left.poll()!! - right.poll()!!)
}

println("Sum of distances values: $result")