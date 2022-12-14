import java.io.File
import java.io.IOException
import java.nio.file.Files


typealias File1X = File
typealias File2X = File
typealias File3X = File

const val RESOLUTION_SUFFIX_1_X = "_x1"
const val RESOLUTION_SUFFIX_2_X = "_x2"
const val RESOLUTION_SUFFIX_3_X = "_x3"
const val RESOLUTION_SUFFIX_LENGTH = 3

class Converter {

    fun convertIosResolutionFilesTo(
        targetAppResPath: String,
        sourceDirPath: String,
        generateDirsIfNeeded: Boolean = true
    ) {
        require(targetAppResPath.isNotEmpty()) { "Empty res path" }
        val resFile = File(targetAppResPath)

        require(resFile.exists()) { "File DNE!" }
        require(resFile.isDirectory) { "File is not a directory" }


        val anyDpi = File(targetAppResPath, "drawable")
        val hdpi = File(targetAppResPath, "drawable-hdpi")
        val ldpi = File(targetAppResPath, "drawable-ldpi")
        val mdpi = File(targetAppResPath, "drawable-mdpi")
        val xhdpi = File(targetAppResPath, "drawable-xhdpi")
        val xxhdpi = File(targetAppResPath, "drawable-xxhdpi")
        val xxxhdpi = File(targetAppResPath, "drawable-xxxhdpi")


        val destinationFiles = listOf(anyDpi, hdpi, ldpi, mdpi, xhdpi, xxhdpi, xxxhdpi)

        destinationFiles.forEach {
            when {
                !it.exists() && !generateDirsIfNeeded -> error("file with name ${it.name} DNE!")
                !it.exists() && generateDirsIfNeeded -> it.createNewFile()
                else -> Unit // NO-OP
            }
        }

        val dirs1x = listOf(anyDpi, ldpi, mdpi)
        val dirs2x = listOf(hdpi, xhdpi)
        val dirs3x = listOf(xxhdpi, xxxhdpi)

        val files = getFilesFromTargetDir(sourceDirPath)
        copyFileToDir(files.first, dirs1x)
        copyFileToDir(files.second, dirs2x)
        copyFileToDir(files.third, dirs3x)
    }

    private fun copyFileToDir(sourceFile: File, dirs: List<File>) {
        dirs.forEach {
            try {
                val originalFullName = sourceFile.name
                val originalNameWithoutExtension = sourceFile.nameWithoutMimeType()
                val dotWithExtension = originalFullName.replace(originalNameWithoutExtension, "")
                val resolutionSuffixDroppedName = originalNameWithoutExtension.dropLast(RESOLUTION_SUFFIX_LENGTH)
                val newFileName = "${resolutionSuffixDroppedName}${dotWithExtension}"
                Files.copy(sourceFile.toPath(), it.toPath().resolve(newFileName))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getFilesFromTargetDir(dirPath: String): Triple<File1X, File2X, File3X> {
        val sourceFile = File(dirPath)
        require(sourceFile.exists()) { "File DNE!" }
        require(sourceFile.isDirectory) { "File is not a directory" }

//        val children = sourceFile.listFiles() ?: error("empty source file")
//        val file1x = children.find { it.name.contains(RESOLUTION_SUFFIX_1_X) } ?: error("_1x file DNE ")
//        val file2x = children.find { it.name.contains(RESOLUTION_SUFFIX_2_X) } ?: error("_2x file DNE ")
//        val file3x = children.find { it.name.contains(RESOLUTION_SUFFIX_3_X) } ?: error("_3x file DNE ")
        require(ensureFileCount(sourceFile)) { "size != 3" }
        val children = sourceFile.listFiles() ?: error("empty source file")
        var file1x: File? = null
        var file2x: File? = null
        var file3x: File? = null
        children.forEach {
            when {
                it.name.contains(RESOLUTION_SUFFIX_1_X) -> file1x = it
                it.name.contains(RESOLUTION_SUFFIX_2_X) -> file2x = it
                it.name.contains(RESOLUTION_SUFFIX_3_X) -> file3x = it
            }
        }

        if (file1x == null) error("_1x file DNE")
        if (file2x == null) error("_2x file DNE")
        if (file3x == null) error("_3x file DNE")


        return Triple(file1x!!, file2x!!, file3x!!)
    }

    private fun ensureFileCount(parentFile: File): Boolean {
        return parentFile.list()?.size == 3
    }

    private fun File.nameWithoutMimeType(): String {
        val index: Int = name.lastIndexOf(".")
        return name.substring(0, index);
    }
}