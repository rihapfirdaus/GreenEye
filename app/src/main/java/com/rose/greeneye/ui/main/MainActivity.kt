package com.rose.greeneye.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.rose.greeneye.R
import com.rose.greeneye.adapter.SectionPagerAdapter
import com.rose.greeneye.databinding.ActivityMainBinding
import com.rose.greeneye.ui.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.getSession().collect { session ->
                if (session) {
                    setContentView(binding.root)
                } else {
                    Intent(this@MainActivity, WelcomeActivity::class.java).also {
                        startActivity(it)
                    }
                }
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this@MainActivity)

        binding.apply {
            viewPager.adapter = sectionPagerAdapter

            viewModel.currentPage.observe(this@MainActivity) { page ->
                if (viewPager.currentItem == page) {
                    binding.viewPager.setCurrentItem(page, true)
                } else {
                    binding.viewPager.setCurrentItem(page, false)
                }
            }

            viewPager.setCurrentItem(1, false)

            bottomNavigationView.selectedItemId = R.id.item_menu_home

            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.item_menu_predict -> viewModel.setCurrentPage(0)
                    R.id.item_menu_home -> viewModel.setCurrentPage(1)
                    R.id.item_menu_mark -> viewModel.setCurrentPage(2)
                }
                true
            }

            viewPager.registerOnPageChangeCallback(
                object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        viewModel.setCurrentPage(position)
                        val itemId =
                            when (position) {
                                0 -> R.id.item_menu_predict
                                1 -> R.id.item_menu_home
                                else -> R.id.item_menu_mark
                            }
                        binding.bottomNavigationView.selectedItemId = itemId
                    }
                },
            )
        }
    }
}
