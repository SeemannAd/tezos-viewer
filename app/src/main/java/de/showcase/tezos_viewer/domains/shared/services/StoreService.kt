package de.showcase.tezos_viewer.domains.shared.services

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.google.modernstorage.storage.AndroidFileSystem
import com.google.modernstorage.storage.toOkioPath
import okio.Path.Companion.toOkioPath
import okio.buffer

data class StoreData(
    val fileName: String,
    val data: String
)

data class StoreResult(
    val success: Boolean,
    val message: String? = null,
    val uri: Uri? = null,
    val data: String? = null
)

class StoreService(val context: Context) {
    private val fileSystem = AndroidFileSystem(context)

    private val collection = MediaStore
        .Files
        .getContentUri("external")
    private var directory = Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

    /**
     * Write only text to storage
     *
     * @fileName: convention is name.extensions, for example text.txt
     */
    suspend fun write(storeData: StoreData): StoreResult {
        lateinit var uri: Uri
        val mimeType = "text/plain"

        // make sure directory exists
        if(!directory.exists()) {
            directory.mkdirs()
        }

        try {
            uri = fileSystem.createMediaStoreUri(
                filename = storeData.fileName,
                collection = collection,
                directory = directory.absolutePath
            )!!
        } catch (e: Exception) {
            return StoreResult(
                success = false,
                message = "Could not create storage Uri!\ncollection= $collection,\ndirectory=$directory,\nmessage=$${e.message}"
            )
        }

        val path = uri.toOkioPath()

        try {
            fileSystem.write(path, false) {
                writeUtf8(storeData.data)
            }
        } catch (e: Exception) {
            return StoreResult(
                success = false,
                message = "Could not write to storage! path=$path"
            )
        }

        // check for saved file uri and meta-data
        fileSystem.scanUri(uri, mimeType)
        val metadata = fileSystem.metadataOrNull(path)

        return StoreResult(
            success = true,
            message = "Successfully stored file.\nUri=$uri,\npath=$path,\nmetadata=$metadata",
            uri = uri,
        )
    }

    /**
     * Read only text to storage
     *
     * @fromFileName: convention is name.extensions, for example text.txt
     */
    suspend fun read(fromFileName: String): StoreResult {
        lateinit var uri: Uri

        try {
            uri = fileSystem.createMediaStoreUri(
                filename = fromFileName,
                collection = collection,
                directory = directory.absolutePath
            )!!
        } catch (e: Exception) {
            return StoreResult(
                success = false,
                message = "Could not create storage Uri!\ncollection= $collection,\ndirectory=$directory,\nmessage=$${e.message}"
            )
        }

        val path = uri.toOkioPath()

        return try {
            val data = fileSystem.source(path).buffer().readUtf8()

            StoreResult(
                success = true,
                message = "Successfully read from Store!",
                uri = uri,
                data = data
            )
        } catch (e: Exception) {
            StoreResult(
                success = false,
                message = "Could not read from Store!\nuri=$uri,\npath=$path",
                uri = uri,
            )
        }
    }

    suspend fun clear() {
        fileSystem.deleteRecursively(directory.toOkioPath())
    }
}