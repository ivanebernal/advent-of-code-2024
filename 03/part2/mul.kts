import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""
val regex = "(?:mul\\((\\d{1,3}),(\\d{1,3})\\))|(?:do\\(\\))|(?:don't\\(\\))".toRegex()
var result = 0
var enabled = true

File(file).forEachLine { line ->
    regex.findAll(line).forEach { match ->
        val instruction = match.value
        when {
            instruction == "do()" -> enabled = true
            instruction == "don't()" -> enabled = false
            else -> {
                if (enabled) {
                    val a = match.groupValues[1].toInt()
                    val b = match.groupValues[2].toInt()
                    result += a * b
                }
            }
        }
    }
}

println("Result: $result")