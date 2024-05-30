package com.rose.greeneye.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.rose.greeneye.Utils.getDummyList
import com.rose.greeneye.adapter.ListItemAdapter
import com.rose.greeneye.data.local.PlantModel
import com.rose.greeneye.databinding.ActivityResultBinding
import com.rose.greeneye.ui.detail.DetailActivity

class ResultActivity : AppCompatActivity(), ListItemAdapter.OnItemClickListener {
    private val binding: ActivityResultBinding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dummyList = getDummyList(this@ResultActivity)

        val listItemAdapter = ListItemAdapter(ListItemAdapter.VERTICAL_TYPE, this)

        binding.apply {
            searchView.apply {
                setupWithSearchBar(searchBar)
                editText.setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    hide()
                    false
                }

                rvResult.layoutManager = GridLayoutManager(this@ResultActivity, 2)
                rvResult.adapter = listItemAdapter

                listItemAdapter.submitList(dummyList)
            }
        }
    }

    override fun onListItemClick(
        position: Int,
        item: PlantModel,
    ) {
        Intent(this@ResultActivity, DetailActivity::class.java).also {
            startActivity(it)
        }
    }
}
