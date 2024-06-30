package com.rose.greeneye.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.rose.greeneye.R
import com.rose.greeneye.databinding.ActivityResultBinding
import com.rose.greeneye.ui.detail.DetailActivity

class ResultActivity : AppCompatActivity() {
    private val binding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            Glide.with(this@ResultActivity)
                .load(intent.getStringExtra(EXTRA_IMAGE_PATH)?.toUri())
                .into(ivResult)

            tvOrgansName.text = intent.getStringExtra(EXTRA_ORGANS)
            tvPlantName.text = intent.getStringExtra(EXTRA_PLANT_NAME)
            tvScientificName.text = intent.getStringExtra(EXTRA_SCIENTIFIC_NAME)
            tvAccuracy.text =
                getString(R.string.akurasi, intent.getFloatExtra(EXTRA_ACCURACY, 0f).toString())

            btnShowDetail.setOnClickListener {
                Intent(this@ResultActivity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_ID_PLANT, intent.getIntExtra(EXTRA_ID_PLANT, -1))
                }.also {
                    startActivity(it)
                }
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_IMAGE_PATH = "extra_image_path"
        const val EXTRA_ID_PLANT = "extra_id_plant"
        const val EXTRA_PLANT_NAME = "extra_plant_name"
        const val EXTRA_SCIENTIFIC_NAME = "extra_scientific_name"
        const val EXTRA_ACCURACY = "extra_accuracy"
        const val EXTRA_ORGANS = "extra_organs"
    }
}
