import java.io.File
import java.util.ArrayDeque
typealias Coord = Pair<Long, Long>

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

var result = 0L

val games = buildGames()
val maxPresses = 100

games.forEach { game ->
    var prize = game.prize
    val buttonA = game.buttons[0]
    val buttonB = game.buttons[1]
    
    val xa = buttonA.increment.first
    val xb = buttonB.increment.first
    val ya = buttonA.increment.second
    val yb = buttonB.increment.second
    val px = prize.first
    val py = prize.second

    val x = ((py * xa - ya * px) * xb) / (yb * xa - ya * xb)
    val y = ((py * xa - ya * px) * yb) / (yb * xa - ya * xb)
    

    val intersection = x to y

    if (intersection.isDivisibleBy(buttonB.increment) && (prize - intersection).isDivisibleBy(buttonA.increment)) {
        result += x / buttonB.increment.first
        result += ((prize.first - x) / buttonA.increment.first) * buttonA.cost
    } else if (intersection.isDivisibleBy(buttonA.increment) && (prize - intersection).isDivisibleBy(buttonB.increment)) {
        result += (x / buttonA.increment.first) * buttonA.cost
        result += (prize.first - x) / buttonB.increment.first 
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
                        increment = captureGroups[2].toLong() to captureGroups[3].toLong(),
                        cost = if(captureGroups[1] == "A") 3L else 1L
                    )
                )
            }
            line.contains("Prize") -> {
                val captureGroups = prizeRegex.matchEntire(line)!!.groupValues
                val x = captureGroups[1].toLong() + 10000000000000L
                val y = captureGroups[2].toLong() + 10000000000000L
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
    val cost: Long
)

data class Game(
    val prize: Coord,
    val buttons: List<Button>
)

operator fun Coord.minus(other: Coord) = (this.first - other.first) to (this.second - other.second)
operator fun Coord.plus(other: Coord) = (this.first + other.first) to (this.second + other.second)
fun Coord.isDivisibleBy(other: Coord) = 
    (this.first % other.first == 0L) && (this.second % other.second == 0L) && first/other.first == second/other.second