package com.rose.greeneye.ui.mark

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rose.greeneye.R
import com.rose.greeneye.adapter.FavoriteItemAdapter
import com.rose.greeneye.data.local.entity.FavoriteEntity
import com.rose.greeneye.databinding.FragmentMarkBinding
import com.rose.greeneye.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkFragment : Fragment(), FavoriteItemAdapter.OnItemClickListener {
    private lateinit var binding: FragmentMarkBinding
    private lateinit var listItemAdapter: FavoriteItemAdapter

    private val viewModel: MarkViewModel by viewModels()

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

        listItemAdapter = FavoriteItemAdapter(this)

        binding.apply {
            rvMark.layoutManager = LinearLayoutManager(requireContext())
            rvMark.adapter = listItemAdapter
            rvMark.addItemDecoration(
                object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State,
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        val vSpacing = resources.getDimensionPixelSize(R.dimen.rv_vSpacing)
                        val hSpacing = resources.getDimensionPixelSize(R.dimen.rv_hSpacing)

                        outRect.set(hSpacing, vSpacing, hSpacing, vSpacing)
                    }
                },
            )
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getFavorites().observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                if (it.isEmpty()) {
                    showEmptyMessage(true)
                } else {
                    showEmptyMessage(false)
                    listItemAdapter.submitList(it)
                }
            }
        }
    }

    private fun showEmptyMessage(state: Boolean) {
        if (state) {
            binding.msgEmpty.visibility = View.VISIBLE
            binding.rvMark.visibility = View.GONE
            playAnimation()
        } else {
            binding.msgEmpty.visibility = View.GONE
            binding.rvMark.visibility = View.VISIBLE
        }
    }

    private fun playAnimation() {
        binding.apply {
            ObjectAnimator.ofFloat(msgEmpty, View.ALPHA, 1f).setDuration(10000).start()
        }
    }

    override fun onListItemClick(
        position: Int,
        item: FavoriteEntity,
    ) {
        Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID_PLANT, item.index)
        }.also {
            startActivity(it)
        }
    }
}
