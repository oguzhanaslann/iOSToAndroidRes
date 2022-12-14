

fun main() {
    val targetAppResPath = "-"
    val sourceDirPath = "-"
    val generateDirsIfNeeded = false
    val converter = Converter()
    converter.convertIosResolutionFilesTo(targetAppResPath, sourceDirPath, generateDirsIfNeeded)
}