package com.keyboardhero.qr.features.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePageAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    private val screen = mutableListOf<Fragment>()

    override fun getItemCount(): Int = screen.size

    override fun createFragment(position: Int): Fragment {
        return screen[position]
    }

    fun addScreen(fragments: List<Fragment>) {
        screen.addAll(fragments)
    }
}