package com.rose.greeneye.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rose.greeneye.R
import com.rose.greeneye.Utils.getBitmap
import com.rose.greeneye.Utils.getFormattedText
import com.rose.greeneye.Utils.saveBitmapToFile
import com.rose.greeneye.Utils.showToast
import com.rose.greeneye.data.local.entity.FavoriteEntity
import com.rose.greeneye.data.local.entity.HistoryEntity
import com.rose.greeneye.data.remote.response.DataPlantResponse
import com.rose.greeneye.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var plant: DataPlantResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val index: Int = intent.getIntExtra(EXTRA_ID_PLANT, -1)

        viewModel.getPlantById(index).observe(this@DetailActivity) { result ->
            result.onSuccess { plantResponse ->
                plant = plantResponse.data
                updateUI(plant)
                addToHistory(plant)
                observeFavoriteStatus(plant)
            }
        }
    }

    private fun addToHistory(plant: DataPlantResponse) {
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.addToHistory(
                    HistoryEntity(
                        scienceName = plant.scienceName,
                        enName = plant.enName,
                        idName = plant.idName,
                        createdAt = System.currentTimeMillis(),
                        index = plant.index,
                        imageUrl = plant.imageUrl[2],
                        benefits = plant.benefits,
                        effects = plant.effects,
                        tips = plant.tips,
                    ),
                ).collect { result ->
                    result.onFailure {
                        showToast(this@DetailActivity, it.message.toString())
                    }
                }
            }
        }
    }

    private fun observeFavoriteStatus(plant: DataPlantResponse) {
        viewModel.isFavoritePlant(plant.scienceName).observe(this@DetailActivity) { isFavorite ->
            updateMark(isFavorite)

            binding.btnMark.setOnClickListener {
                if (isFavorite) {
                    lifecycleScope.launchWhenResumed {
                        launch {
                            viewModel.removeFavoritePlant(plant.scienceName).collect { result ->
                                result.onSuccess {
                                    showToast(
                                        this@DetailActivity,
                                        getString(R.string.toast_mark_plant_removed),
                                    )
                                    updateMark(false)
                                }
                                result.onFailure {
                                    showToast(
                                        this@DetailActivity,
                                        getString(R.string.toast_mark_plant_remove_failed),
                                    )
                                }
                            }
                        }
                    }
                } else {
                    lifecycleScope.launchWhenResumed {
                        launch {
                            viewModel.addToFavorite(
                                FavoriteEntity(
                                    scienceName = plant.scienceName,
                                    enName = plant.enName,
                                    idName = plant.idName,
                                    createdAt = System.currentTimeMillis(),
                                    index = plant.index,
                                    imageUrl = plant.imageUrl[2],
                                    benefits = plant.benefits,
                                    effects = plant.effects,
                                    tips = plant.tips,
                                ),
                            ).collect { result ->
                                result.onSuccess {
                                    showToast(
                                        this@DetailActivity,
                                        getString(R.string.toast_mark_plant_added),
                                    )
                                    updateMark(true)
                                }
                                result.onFailure {
                                    showToast(
                                        this@DetailActivity,
                                        getString(R.string.toast_mark_plant_add_failed),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateMark(state: Boolean) {
        binding.btnMark.setImageDrawable(
            ContextCompat.getDrawable(
                this@DetailActivity,
                if (state) R.drawable.ic_bookmark_solid else R.drawable.ic_bookmark_outline,
            ),
        )
    }

    private fun updateUI(plant: DataPlantResponse) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(plant.imageUrl[2])
                .into(ivPlant)
            tvPlantName.text = plant.idName
            tvScientificName.text = plant.scienceName
            tvPlantInfo.text = getFormattedText(plant)

            btnShare.setOnClickListener {
                val uri = saveBitmapToFile(this@DetailActivity, ivPlant.getBitmap())
                val message = tvPlantInfo.text.toString()
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
        val shareIntent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, text)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
    }

    companion object {
        const val EXTRA_ID_PLANT = "extra_id_plant"
    }
}
