

fun main() {
    val targetAppResPath = "/.../.../.../..."
    val sourceDirPath = "/.../.../.../..."
    val generateDirsIfNeeded = true
    val converter = Converter()
    converter.convertIosResolutionFilesTo(targetAppResPath, sourceDirPath, generateDirsIfNeeded)
}