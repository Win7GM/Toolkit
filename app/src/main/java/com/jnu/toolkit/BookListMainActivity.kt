package com.jnu.toolkit

import android.os.Bundle
import android.view.Menu
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
                1 -> tab.text = "News"
                2 -> tab.text = "Seller"
            }
        }.attach()
    }

    private class FragmentAdapter(val fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> BookItemFragment.newInstance()
                else -> WebViewFragment.newInstance()
            }
        }

    }

    // creating context menu by simply calling a function and passing the menu to it
    fun createMenu(menu: Menu) {
        val groupID = 0
        val order = 0
        val itemID = intArrayOf(1, 2, 3)
        for (i in itemID.indices) {
            when (itemID[i]) {
                1 -> menu.add(groupID, itemID[i], order, R.string.menu_insert)
                2 -> menu.add(groupID, itemID[i], order, R.string.menu_edit)
                3 -> menu.add(groupID, itemID[i], order, R.string.menu_delete)
            }
        }
    }
}
