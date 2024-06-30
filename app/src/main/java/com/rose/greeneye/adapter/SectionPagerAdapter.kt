package com.rose.greeneye.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rose.greeneye.ui.dashboard.DashboardFragment
import com.rose.greeneye.ui.mark.MarkFragment
import com.rose.greeneye.ui.predict.PredictFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = PredictFragment()
            1 -> fragment = DashboardFragment()
            2 -> fragment = MarkFragment()
        }

        return fragment as Fragment
    }
}
