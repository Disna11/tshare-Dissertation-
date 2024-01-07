package com.example.tshare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class tabLayoutAdapter(fm: FragmentManager, behavior: Int): FragmentPagerAdapter(fm, behavior) {

    val fragmentArrayList: ArrayList<Fragment> = ArrayList()
    val fragmentTitle: ArrayList<String> = ArrayList()

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentArrayList.add(fragment)
        fragmentTitle.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

}