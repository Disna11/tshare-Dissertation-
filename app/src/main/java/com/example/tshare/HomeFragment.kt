package com.example.tshare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var tab: TabLayout
    private lateinit var viewpager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tab = view.findViewById(R.id.homeFragmentTabLayout)
        viewpager = view.findViewById(R.id.homeFragmentViewPager)

        tab.setupWithViewPager(viewpager)
        val tabLayoutAdapter = tabLayoutAdapter(childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        tabLayoutAdapter.addFragment(rideOfferFragment(), "RIDE OFFERS")
        tabLayoutAdapter.addFragment(taxiShareFragment(), "TAXI SHARE")
        viewpager.adapter = tabLayoutAdapter

        return view
    }
}
