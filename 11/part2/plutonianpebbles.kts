import java.io.File
import java.util.ArrayDeque

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

var stones = File(file).readLines().first().split(" ")
val blinks = 75
val memo = mutableMapOf<Pair<String, Int>, Long>()

val finalStoneCount = stones.fold(0L) { acc, stone -> acc + getCount(stone, blinks) }

println("Result: $finalStoneCount")

fun getCount(stone: String, blinks: Int): Long {
    return if (blinks == 0) {
        return 1L
    } else if (stone == "0") {
        val m = "1" to blinks - 1
        val count = memo[m] ?: getCount("1", blinks - 1)
        memo[m] = count
        count
    } else if (stone.length % 2 == 0) {
        val stone1 = stone.substring(0, stone.length/2).removeLeadingZeroes()
        val stone2 = stone.substring(stone.length/2).removeLeadingZeroes()
        val m1 = stone1 to blinks - 1
        val m2 = stone2 to blinks - 1
        val count1 = memo[m1] ?: getCount(stone1, blinks - 1)
        val count2 = memo[m2] ?: getCount(stone2, blinks - 1)
        memo[m1] = count1
        memo[m2] = count2
        count1 + count2
    } else {
        val newStone = (stone.toLong() * 2024).toString()
        val m = newStone to blinks - 1 
        val count = memo[m] ?: getCount(newStone, blinks - 1)
        memo[m] = count
        count
    }
}

fun String.removeLeadingZeroes(): String {
    val firstNonZero = this.indexOfFirst { it != '0' }
    if (firstNonZero == -1) return "0"
    return substring(firstNonZero)
}