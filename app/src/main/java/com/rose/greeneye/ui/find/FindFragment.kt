package com.rose.greeneye.ui.find

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.rose.greeneye.R
import com.rose.greeneye.Utils.getImageUri
import com.rose.greeneye.Utils.showToast
import com.rose.greeneye.databinding.FragmentFindBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindFragment : Fragment() {
    private lateinit var binding: FragmentFindBinding
    private var currentImageUri: Uri? = null

    private val viewModel: FindViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFindBinding.inflate(inflater, container, false)
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
        }
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
