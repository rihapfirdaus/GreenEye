package com.rose.greeneye.ui.predict

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.rose.greeneye.R
import com.rose.greeneye.Utils.getImageUri
import com.rose.greeneye.Utils.reduceFileImage
import com.rose.greeneye.Utils.showToast
import com.rose.greeneye.Utils.uriToFile
import com.rose.greeneye.databinding.FragmentPredictBinding
import com.rose.greeneye.ui.result.ResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@AndroidEntryPoint
class PredictFragment : Fragment() {
    private lateinit var binding: FragmentPredictBinding
    private var currentImageUri: Uri? = null

    private val viewModel: PredictViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPredictBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentImageUri.observe(
            viewLifecycleOwner,
        ) {
            if (it != null) {
                showImage(it)
            } else {
                showPreview(false)
            }
        }

        binding.apply {
            btnGallery.setOnClickListener {
                startGallery()
            }

            btnCamera.setOnClickListener {
                startCamera()
            }

            btnChange.setOnClickListener {
                viewModel.setCurrentImageUri(null)
            }

            btnScan.setOnClickListener {
                handlePredict()
            }
        }
    }

    private fun handlePredict() {
        currentImageUri?.let { uri ->
            binding.progressCircular.visibility = View.VISIBLE

            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile,
                )

            lifecycleScope.launch {
                viewModel.predictPlant(file = multipartBody).collect { response ->
                    response.onSuccess {
                        binding.progressCircular.visibility = View.GONE

                        it.data.also {
                            Intent(requireContext(), ResultActivity::class.java).apply {
                                putExtra(ResultActivity.EXTRA_IMAGE_PATH, currentImageUri.toString())
                                putExtra(ResultActivity.EXTRA_ID_PLANT, it.index)
                                putExtra(ResultActivity.EXTRA_PLANT_NAME, it.plants.idName)
                                putExtra(ResultActivity.EXTRA_SCIENTIFIC_NAME, it.plants.scienceName)
                                putExtra(ResultActivity.EXTRA_ORGANS, it.organs)
                                putExtra(ResultActivity.EXTRA_ACCURACY, it.accuracy)
                            }.also {
                                startActivity(it)
                            }
                        }
                    }
                    response.onFailure {
                        binding.progressCircular.visibility = View.GONE
                        showToast(requireContext(), "${it.message}")
                    }
                }
            }
        } ?: showToast(requireContext(), getString(R.string.toast_pick_image_first))
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                viewModel.setCurrentImageUri(uri)
            } else {
                showToast(requireContext(), getString(R.string.toast_failed_pick_image))
            }
        }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera =
        registerForActivityResult(
            ActivityResultContracts.TakePicture(),
        ) { isSucces ->
            if (isSucces) {
                viewModel.setCurrentImageUri(currentImageUri)
            } else {
                showToast(requireContext(), getString(R.string.toast_failed_pick_image))
            }
        }

    private fun showImage(uri: Uri) {
        showPreview(true)
        Glide.with(requireContext())
            .load(currentImageUri)
            .into(binding.ivPreview)
    }

    private fun showPreview(state: Boolean) {
        binding.apply {
            if (state) {
                lnUpload.visibility = View.GONE
                lnPreview.visibility = View.VISIBLE
            } else {
                lnUpload.visibility = View.VISIBLE
                lnPreview.visibility = View.GONE
            }
        }
    }
}
