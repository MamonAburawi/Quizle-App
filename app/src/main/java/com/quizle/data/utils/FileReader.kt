package com.quizle.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.quizle.common.utils.TimeFormatter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


// Other imports for Bitmap compression

class FileReader(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun uriToFileInfo(contentUri: Uri): FileInfo {
        return withContext(ioDispatcher) {
            Log.d("FilePicker", "init data")

            val originalBytes = context.contentResolver.openInputStream(contentUri)?.use { it.readBytes() } ?: byteArrayOf()
            val compressedBytes = compressImage(originalBytes)
            val fileSizeInKb = compressedBytes.size.toKb()

            Log.d("FilePicker", "init data2")
            val mimeType = context.contentResolver.getType(contentUri)
            val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            val fileName = "${TimeFormatter.getCurrentTimeForFileName()}.${fileExtension}"

            Log.d("FilePicker", "File Name: $fileName \n File Extension: $fileExtension \n File MimeType: $mimeType \n File Path: ${contentUri.path} \n sizeInKB: ${fileSizeInKb}KB")

            FileInfo(
                name = fileName,
                mimeType = mimeType ?: "",
                bytes = compressedBytes,
                sizeInKb = fileSizeInKb
            )
        }
    }

    private fun compressImage(originalBytes: ByteArray, quality: Int = 70): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }

    class FileInfo(
        val name: String,
        val mimeType: String,
        val bytes: ByteArray,
        val sizeInKb: Int
    )


}



//fun Int.toKb(): Int = "%.1f".format(this / 1024).toDouble()
