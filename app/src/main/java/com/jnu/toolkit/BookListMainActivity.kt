package com.jnu.toolkit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class BookListMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_list_activity_main)

        val viewPagerFragments = findViewById<ViewPager2>(R.id.viewpager2_content)
        viewPagerFragments.adapter = FragmentAdapter(this)

        val tabLayoutHeader: TabLayout = findViewById(R.id.tablayout_header)
        TabLayoutMediator(
            tabLayoutHeader, viewPagerFragments
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Book"
                1 -> tab.text = "消息"
                2 -> tab.text = "卖家"
                3 -> tab.text = "游戏"
            }
        }.attach()
    }

    private class FragmentAdapter(val fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 4
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> BookItemFragment.newInstance()
                1 -> WebViewFragment.newInstance()
                2 -> MapFragment.newInstance()
                else -> GameFragment.newInstance()
            }
        }

    }


}
