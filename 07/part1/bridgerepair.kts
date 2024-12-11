import java.io.File
import java.util.ArrayDeque

val file = args[1 + args.indexOf("-i")]

var result = 0L

File(file).forEachLine { line ->
    val (calib, values) = line.split(": ").let { it[0].toLong() to it[1].split(" ").map { it.toLong() } }
    val results = ArrayDeque<Long>()
    for (value in values) {
        if (results.isEmpty()) {
            results.add(value)
            continue
        }
        val temp = ArrayDeque<Long>()
        while(!results.isEmpty()) {
            val curr = results.removeFirst()
            temp.add(curr * value)
            temp.add(curr + value)
        }
        results.addAll(temp)
    }
    // println(results)
    if (results.contains(calib)) result += calib
}

println("Return: $result")