import java.io.File

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val blocks = File(file).readLines().first()

val nonEmptySize = blocks.filterIndexed { idx, _ -> idx % 2 == 0 }.fold(0) { acc, c ->  acc + (c - '0') }
var blockLeft = 0
var blockLeftStart = 0
val blockLeftSize: Int
    get() = blocks[blockLeft] - '0'
var blockRight = blocks.length - 1
val blockRightSize: Int
    get() = blocks[blockRight] - '0'
var rightRead = 0

var checksum = 0L
var i = 0

while (i < nonEmptySize) {
    if (i >= blockLeftStart + blockLeftSize) {
        blockLeftStart = i
        blockLeft++
        if (blockLeftSize == 0) continue
    }
    if (rightRead >= blockRightSize) {
        rightRead = 0
        blockRight -= 2
        if (blockRightSize == 0) continue
    }
    if (blockLeft % 2 == 0) {
        val id = blockLeft / 2
        if (isTest) print(id)
        checksum += i * id
    } else {
        val id = blockRight / 2
        checksum += i * id
        if (isTest) print(id)
        rightRead++
    }
    i++
}

if (isTest) println()

println(checksum)