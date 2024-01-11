package com.example.tshare.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.tshare.R
import com.example.tshare.adapter.tabLayoutAdapter
import com.google.android.material.tabs.TabLayout


class historyFragment : Fragment() {

    private lateinit var tab: TabLayout
    private lateinit var viewpager: ViewPager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            val view= inflater.inflate(R.layout.fragment_history, container, false)
        tab = view.findViewById(R.id.homeFragmentTabLayout)
        viewpager = view.findViewById(R.id.homeFragmentViewPager)

        tab.setupWithViewPager(viewpager)
        val tabLayoutAdapter = tabLayoutAdapter(childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        tabLayoutAdapter.addFragment(historyRideOfferFragment(), "RIDE OFFERS")
        tabLayoutAdapter.addFragment(historyTaxiShareFragment(), "TAXI SHARE")
        viewpager.adapter = tabLayoutAdapter

        return view
    }


}