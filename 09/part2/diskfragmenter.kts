import java.io.File

val file = args[1 + args.indexOf("-i")]
val isTest = args.contains("-t")

val blocks = File(file).readLines().first()

val size = blocks.fold(0) { acc, c ->  acc + (c - '0') }
var blockLeft = 0
val blockLeftSize: Int
    get() = blocks[blockLeft] - '0'

var checksum = 0L
var i = 0
val read = mutableSetOf<Int>()

while (i < size) {
    if (blockLeft % 2 == 0) {
        if (read.contains(blockLeft)) {
            i += blockLeftSize
            blockLeft++
            continue
        }
        checksum += calculateBlockChecksum(i, blockLeftSize, blockLeft / 2L)
        i += blockLeftSize
        read.add(blockLeft++)
    } else {
        var nextBlock = blocks.length - 1
        var spaceToFill = blockLeftSize
        var start = i
        while (nextBlock >= 0 && spaceToFill > 0) {
            val nextBlockSize = blocks[nextBlock] - '0'
            if (nextBlockSize <= spaceToFill) {
                if (read.contains(nextBlock)) {
                    nextBlock-=2
                    continue
                }
                read.add(nextBlock)
                checksum += calculateBlockChecksum(start, nextBlockSize, nextBlock / 2L)
                start += nextBlockSize
                spaceToFill -= nextBlockSize
            }
            nextBlock-=2
        }
        i += blockLeftSize
        blockLeft++
    }
}

if (isTest) println()

fun calculateBlockChecksum(start: Int, size: Int, id: Long): Long = (((size * (size - 1)) / 2) + (start * size)) * id

println(checksum)