import java.io.File
import java.util.ArrayDeque

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
    if(!isUpdateValid(pages)) {
        val reordered = reorderPages(pages)
        result += reordered[reordered.size/2]
    }
}

fun isUpdateValid(pages: List<Int>): Boolean {
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
    return isValid
}

fun reorderPages(pages: List<Int>): List<Int> {
    val queue = ArrayDeque<Int>()
    for (page in pages) {
        if (queue.isEmpty()) {
            queue.addFirst(page)
            continue
        }
        val pageRule = ruleMap[page] ?: setOf()
        var place = 0
        val temp = ArrayDeque<Int>()
        while (!queue.isEmpty()) {
            val next = queue.removeFirst()
            temp.addLast(next)
            if (pageRule.contains(next)) {
                place = temp.size
            }
        }
        var curr = 0
        while(!temp.isEmpty()) {
            if (curr == place) {
                curr++
                queue.addLast(page)
                continue
            }
            curr++
            queue.addLast(temp.removeFirst())
        }
        if (curr == place) {
            queue.addLast(page)
        }
    }
    return queue.toList().reversed()
}

println("Result: $result")