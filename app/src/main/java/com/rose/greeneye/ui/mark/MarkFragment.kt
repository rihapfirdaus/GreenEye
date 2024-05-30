package com.rose.greeneye.ui.mark

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rose.greeneye.databinding.FragmentMarkBinding

class MarkFragment : Fragment() {
    private lateinit var binding: FragmentMarkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        showEmptyMessage(true)
    }

    private fun showEmptyMessage(state: Boolean) {
        if (state) {
            binding.msgEmpty.visibility = View.VISIBLE
            playAnimation()
        } else {
            binding.msgEmpty.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        binding.apply {
            ObjectAnimator.ofFloat(msgEmpty, View.ALPHA, 1f).setDuration(10000).start()
        }
    }
}
