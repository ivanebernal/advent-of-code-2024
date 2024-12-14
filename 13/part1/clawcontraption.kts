import java.io.File
import java.util.ArrayDeque
typealias Coord = Pair<Int, Int>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

var result = 0

val games = buildGames()
val maxPresses = 100

games.forEach { game ->
    var prize = game.prize
    val buttonA = game.buttons[0]
    val buttonB = game.buttons[1]
    var price = 0
    // var presses = 0
    while(prize.first > 0 && prize.second > 0 && !prize.isDivisibleBy(buttonB.increment)) {
        price += buttonA.cost
        prize -= buttonA.increment
        // presses++
    }
    if(prize.first != 0 && prize.second != 0 && prize.isDivisibleBy(buttonB.increment)) {
        val buttonBPresses = prize.first / buttonB.increment.first
        result += buttonBPresses * buttonB.cost
        prize = 0 to 0
        // presses += buttonBPresses
    }
    if (prize == 0 to 0) {
        result += price
    }
}

println("Min cost: ${result}")


fun buildGames(): List<Game> {
    val buttons = mutableListOf<Button>()
    val games = mutableListOf<Game>()
    val buttonRegex = "Button (\\w): X\\+(\\d*), Y\\+(\\d*)".toRegex()
    val prizeRegex = "Prize: X=(\\d*), Y=(\\d*)".toRegex()
    File(file).forEachLine { line ->
        when {
            line.contains("Button") -> {
                val captureGroups = buttonRegex.matchEntire(line)!!.groupValues
                buttons.add(
                    Button(
                        increment = captureGroups[2].toInt() to captureGroups[3].toInt(),
                        cost = if(captureGroups[1] == "A") 3 else 1
                    )
                )
            }
            line.contains("Prize") -> {
                val captureGroups = prizeRegex.matchEntire(line)!!.groupValues
                val x = captureGroups[1].toInt()
                val y = captureGroups[2].toInt()
                games.add(
                    Game(
                        prize = x to y,
                        buttons = buttons.toList()
                    )
                )
            }
            else -> buttons.clear()
        }
    }
    return games
}

data class Button(
    val increment: Coord,
    val cost: Int
)

data class Game(
    val prize: Coord,
    val buttons: List<Button>
)

data class Turn(
    val cost: Int,
    val pos: Coord,
    val presses: Int
)

operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
fun Coord.inRange(limit: Coord) = (this.first in 0 until limit.first) && (this.second in 0 until limit.second)
operator fun Array<Array<Int>>.get(coord: Coord) = this[coord.first][coord.second]
fun Coord.isDivisibleBy(other: Coord) = 
(this.first % other.first == 0) && (this.second % other.second == 0) && first/other.first == second/other.second