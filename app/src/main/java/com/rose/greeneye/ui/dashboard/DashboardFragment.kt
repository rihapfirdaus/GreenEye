package com.rose.greeneye.ui.dashboard

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.rose.greeneye.R
import com.rose.greeneye.Utils.getDummyList
import com.rose.greeneye.adapter.CarouselAdapter
import com.rose.greeneye.adapter.ListItemAdapter
import com.rose.greeneye.data.local.PlantModel
import com.rose.greeneye.databinding.FragmentDashboardBinding
import com.rose.greeneye.ui.detail.DetailActivity
import com.rose.greeneye.ui.result.ResultActivity

class DashboardFragment :
    Fragment(),
    CarouselAdapter.OnCarouselClickListener,
    ListItemAdapter.OnItemClickListener {
    private lateinit var binding: FragmentDashboardBinding

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

        val dummyList = getDummyList(requireContext())

        val carouselAdapter = CarouselAdapter(dummyList, requireContext(), this)
        val listItemAdapter = ListItemAdapter(ListItemAdapter.HORIZONTAL_TYPE, this)

        binding.apply {
            rvPopular.layoutManager = CarouselLayoutManager()
            rvPopular.adapter = carouselAdapter

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

            listItemAdapter.submitList(dummyList)

            searchBar.setOnClickListener {
                Intent(requireContext(), ResultActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onCarouselClick(
        position: Int,
        item: PlantModel,
    ) {
        Intent(requireContext(), DetailActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onListItemClick(
        position: Int,
        item: PlantModel,
    ) {
        Intent(requireContext(), DetailActivity::class.java).also {
            startActivity(it)
        }
    }
}
