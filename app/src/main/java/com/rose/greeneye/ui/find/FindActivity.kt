package com.rose.greeneye.ui.find

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.rose.greeneye.adapter.ListItemAdapter
import com.rose.greeneye.data.remote.response.DataPlantResponse
import com.rose.greeneye.databinding.ActivityFindBinding
import com.rose.greeneye.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FindActivity : AppCompatActivity(), ListItemAdapter.OnItemClickListener {
    private val binding: ActivityFindBinding by lazy {
        ActivityFindBinding.inflate(layoutInflater)
    }

    private lateinit var listItemAdapter: ListItemAdapter
    private val viewModel: FindViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        listItemAdapter = ListItemAdapter(ListItemAdapter.VERTICAL_TYPE, this)

        binding.apply {
            searchView.apply {
                setupWithSearchBar(searchBar)
                editText.setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    handleSearch(searchView.text.toString())
                    hide()
                    false
                }

                rvResult.layoutManager = GridLayoutManager(this@FindActivity, 2)
                rvResult.adapter = listItemAdapter
            }
        }
    }

    private fun handleSearch(keyword: String) {
        lifecycleScope.launch {
            viewModel.searchPlant(keyword).collect { response ->
                response.onSuccess {
                    if (it.data.isEmpty()) {
                        binding.apply {
                            msgEmpty.visibility = View.VISIBLE
                            lnResult.visibility = View.GONE

                            btnBack.setOnClickListener {
                                finish()
                            }
                        }
                    } else {
                        binding.apply {
                            msgEmpty.visibility = View.GONE
                            lnResult.visibility = View.VISIBLE

                            btnDashboard.setOnClickListener {
                                finish()
                            }
                        }
                        listItemAdapter.submitList(it.data)
                    }
                }
            }
        }
    }

    override fun onListItemClick(
        position: Int,
        item: DataPlantResponse,
    ) {
        Intent(this@FindActivity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID_PLANT, item.index)
        }.also {
            startActivity(it)
        }
    }
}
