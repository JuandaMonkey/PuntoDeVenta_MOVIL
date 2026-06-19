package com.example.pv_movil.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pv_movil.AdministrarClientes

class viewPagerAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2;

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdministrarClientes()
            else -> AdministrarClientes()
        }
    }
}