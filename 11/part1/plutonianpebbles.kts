import java.io.File
import java.util.ArrayDeque

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

var stones = File(file).readLines().first().split(" ")
val blinks = 25

repeat(blinks) {
    val currStones = stones
    val newStones = mutableListOf<String>()
    for (stone in currStones) {
        if (stone == "0") {
            newStones.add("1")
        } else if (stone.length % 2 == 0) {
            val stone1 = stone.substring(0, stone.length/2).removeLeadingZeroes()
            val stone2 = stone.substring(stone.length/2).removeLeadingZeroes()
            newStones.add(stone1)
            newStones.add(stone2)
        } else {
            val newStone = (stone.toLong() * 2024).toString()
            newStones.add(newStone)
        }
    }
    stones = newStones
}

fun String.removeLeadingZeroes(): String {
    val firstNonZero = this.indexOfFirst { it != '0' }
    if (firstNonZero == -1) return "0"
    return substring(firstNonZero)
}

println("Result: ${stones.size}")