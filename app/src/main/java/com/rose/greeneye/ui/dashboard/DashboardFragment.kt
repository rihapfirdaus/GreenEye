package com.rose.greeneye.ui.dashboard

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
import com.google.android.material.carousel.CarouselLayoutManager
import com.rose.greeneye.R
import com.rose.greeneye.Utils.showToast
import com.rose.greeneye.adapter.CarouselAdapter
import com.rose.greeneye.adapter.HistoryItemAdapter
import com.rose.greeneye.data.local.entity.HistoryEntity
import com.rose.greeneye.data.remote.response.DataPlantResponse
import com.rose.greeneye.databinding.FragmentDashboardBinding
import com.rose.greeneye.ui.detail.DetailActivity
import com.rose.greeneye.ui.find.FindActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment :
    Fragment(),
    CarouselAdapter.OnCarouselClickListener,
    HistoryItemAdapter.OnItemClickListener {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var listItemAdapter: HistoryItemAdapter
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlants().observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                val carouselAdapter = CarouselAdapter(it.data, requireContext(), this)
                binding.apply {
                    tvPopular.visibility = View.VISIBLE
                    rvPopular.visibility = View.VISIBLE
                    rvPopular.layoutManager = CarouselLayoutManager()
                    rvPopular.adapter = carouselAdapter
                }
            }
            response.onFailure {
                binding.apply {
                    tvPopular.visibility = View.GONE
                    rvPopular.visibility = View.GONE
                }
                showToast(requireContext(), getString(R.string.toast_failed_fetch_data))
            }
        }

        listItemAdapter = HistoryItemAdapter(this)

        binding.apply {
            rvHistory.layoutManager = LinearLayoutManager(requireContext())
            rvHistory.adapter = listItemAdapter
            rvHistory.addItemDecoration(
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

            searchBar.setOnClickListener {
                Intent(requireContext(), FindActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getHistories().observe(viewLifecycleOwner) { response ->
            response.onSuccess {
                if (it.isEmpty()) {
                    binding.tvHistory.visibility = View.GONE
                    binding.rvHistory.visibility = View.GONE
                } else {
                    binding.tvHistory.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.VISIBLE

                    listItemAdapter.submitList(it)
                }
            }
        }
    }

    override fun onCarouselClick(
        position: Int,
        item: DataPlantResponse,
    ) {
        Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID_PLANT, item.index)
        }.also {
            startActivity(it)
        }
    }

    override fun onListItemClick(
        position: Int,
        item: HistoryEntity,
    ) {
        Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID_PLANT, item.index)
        }.also {
            startActivity(it)
        }
    }
}
