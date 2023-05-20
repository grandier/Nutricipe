package com.capstone.nutricipe.ui.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.capstone.nutricipe.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

fun generateTimeStamp(): String {
    return SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
}

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(generateTimeStamp(), ".jpg", storageDir)
}

fun createFile(context: Context): File {
    val mediaDir = context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES).firstOrNull()?.let {
        File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir

    return File(outputDirectory, "${generateTimeStamp()}.jpg")
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    val rotation = if (isBackCamera) 90f else -90f
    matrix.postRotate(rotation)
    if (!isBackCamera) {
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}


fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)

    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)

    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}

fun rotateFile(file: File, isBackCamera: Boolean = false) {
    val matrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)

    // Apply rotation and scaling transformations to the bitmap
    val rotation = if (isBackCamera) 90f else -90f
    matrix.postRotate(rotation)
    if (!isBackCamera) {
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
    }

    // Create a new rotated bitmap without modifying the original file
    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    // Save the rotated bitmap to the original file
    val fileOutputStream = FileOutputStream(file)
    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    fileOutputStream.flush()
    fileOutputStream.close()
}