import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""
val mulRegex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()
var result = 0

File(file).forEachLine { line ->
    mulRegex.findAll(line).forEach { match ->
        val a = match.groupValues[1].toInt()
        val b = match.groupValues[2].toInt()
        result += a * b
    }
}

println("Result: $result")