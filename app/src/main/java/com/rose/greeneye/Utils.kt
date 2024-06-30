package com.rose.greeneye

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.airbnb.lottie.BuildConfig
import com.rose.greeneye.data.remote.response.DataPlantResponse
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    private const val MAXIMAL_SIZE = 1000000
    private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    fun showToast(
        context: Context,
        message: String,
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getImageUri(context: Context): Uri {
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues =
                ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
                }
            uri =
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues,
                )
        }
        return uri ?: getImageUriForPreQ(context)
    }

    fun uriToFile(
        imageUri: Uri,
        context: Context,
    ): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
            buffer,
            0,
            length,
        )
        outputStream.close()
        inputStream.close()
        return myFile
    }

    fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile,
        )
    }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    private fun Bitmap.getRotatedBitmap(file: File): Bitmap {
        val orientation =
            ExifInterface(file).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED,
            )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
            ExifInterface.ORIENTATION_NORMAL -> this
            else -> this
        }
    }

    private fun rotateImage(
        source: Bitmap,
        angle: Float,
    ): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.width,
            source.height,
            matrix,
            true,
        )
    }

    fun ImageView.getBitmap(): Bitmap {
        this.isDrawingCacheEnabled = true
        this.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(this.drawingCache)
        this.isDrawingCacheEnabled = false
        return bitmap
    }

    fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap,
    ): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "shared_image_${System.currentTimeMillis()}.png")
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    fun getFormattedText(plant: DataPlantResponse): SpannableString {
        val text = "${plant.description}\n\nBenefit:\n${plant.benefits}\n\nEfek:\n${plant.effects}\n\nTips:\n${plant.tips}"

        val spannableString = SpannableString(text)

        val benefitStart = text.indexOf("Benefit:")
        val benefitEnd = benefitStart + "Benefit:".length
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            benefitStart,
            benefitEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        val efekStart = text.indexOf("Efek:")
        val efekEnd = efekStart + "Efek:".length
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            efekStart,
            efekEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        val tipsStart = text.indexOf("Tips:")
        val tipsEnd = tipsStart + "Tips:".length
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            tipsStart,
            tipsEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
        )

        return spannableString
    }
}
