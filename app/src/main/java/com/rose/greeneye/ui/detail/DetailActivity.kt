package com.rose.greeneye.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rose.greeneye.R
import com.rose.greeneye.Utils.getBitmap
import com.rose.greeneye.Utils.saveBitmapToFile
import com.rose.greeneye.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btnShare.setOnClickListener {
                val uri = saveBitmapToFile(this@DetailActivity, ivPlant.getBitmap())
                val message: String = tvPlantInfo.text.toString()

                shareImageAndText(uri, message)
            }

            btnClose.setOnClickListener {
                finish()
            }
        }
    }

    private fun shareImageAndText(
        uri: Uri,
        text: String,
    ) {
        val shareIntent: Intent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, text)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
    }
}
