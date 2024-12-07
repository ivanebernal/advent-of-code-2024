import java.io.File

val file = if(args.contains("-i")) args[1 + args.indexOf("-i")] else ""

val lines = File(file).readLines()
val (rules, updates) = lines.indexOf("").let { breakIdx ->
    lines.subList(0, breakIdx) to lines.subList(breakIdx + 1, lines.size)
}

val ruleMap = mutableMapOf<Int, Set<Int>>()

rules.forEach { rule ->
    val (first, second) = rule.split("|").map { it.toInt() }.let { it[0] to it[1] }
    ruleMap[first] = (ruleMap[first] ?: setOf()) + setOf(second)
}

var result = 0
for (update in updates) {
    val pages = update.split(",").map { it.toInt() }
    var isValid = true
    for (i in 1 until pages.size) {
        for (j in 0 until i) {
            val numbers = ruleMap[pages[i]]
            if (numbers?.contains(pages[j]) == true) {
                isValid = false
                break
            }
        }
        if (!isValid) break
    }
    if (isValid) {
        result += pages[pages.size/2]
    }
}

println("Result: $result")