package com.rose.greeneye.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rose.greeneye.databinding.ActivityWelcomeBinding
import com.rose.greeneye.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        playAnimation()

        binding.btnContinue.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                viewModel.saveSession(true)

                showLoading(true)
            }

            viewModel.getSession().observe(this@WelcomeActivity) { session ->
                if (session) {
                    Intent(this@WelcomeActivity, MainActivity::class.java).also {
                        startActivity(it)
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        binding.apply {
            val tvDesc = ObjectAnimator.ofFloat(tvDesc, View.ALPHA, 1f).setDuration(3000)
            val btnContinue = ObjectAnimator.ofFloat(btnContinue, View.ALPHA, 1f).setDuration(2000)
            val tvAgreement = ObjectAnimator.ofFloat(tvAgreement, View.ALPHA, 1f).setDuration(1000)

            AnimatorSet().apply {
                playSequentially(tvDesc, btnContinue, tvAgreement)
                start()
            }
        }
    }
}
