package com.day.palette.presentation.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.day.palette.R
import com.day.palette.databinding.ActivityMainBinding
import com.day.palette.presentation.ui.main.explore.ExploreFragment
import com.day.palette.presentation.ui.main.home.HomeFragment
import com.day.palette.presentation.ui.main.memories.MemoriesFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.viewmodel.observe

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        ViewCompat.setOnApplyWindowInsetsListener(b.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        vm.observe(this, state = ::observeState, sideEffect = ::observeIntent)

        //Perform all the UI setup here
        setUpBottomBar()

        //Check if UI component is recreating itself
        if (savedInstanceState != null) {
            // Restore the state of the BottomNavigationView
            val selectedItemId =
                savedInstanceState.getInt(INSTANCE_BOTTOM_BAR_SELECTED_ITEM, R.id.bottom_bar_home)
            b.mainActivityBottomBar.selectedItemId = selectedItemId
        }

    }

    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: MainState) {
        //
    }

    /**Observe side effects using Orbit StateFlow*/
    private fun observeIntent(intent: MainSideEffect) {
        when (intent) {
            is MainSideEffect.ShowToast -> {
                Toast.makeText(this@MainActivity, intent.message.asString(this), Toast.LENGTH_SHORT)
                    .show()
            }

            is MainSideEffect.ShowSnack -> {
                Snackbar.make(b.root, intent.message.asString(this), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    /**Set up the bottom navigation bar with swipe-to-change-tab functionality,
     * ensuring both the BottomNavigationView and ViewPager2 are synchronized.*/
    private fun setUpBottomBar() {

        /**By default, BottomNavigationView doesn't support swiping between tabs.
         * To enable this behavior, implement a callback on ViewPager2.*/
        b.mainActivityPager.apply {
            currentItem = 0
            adapter = MainViewPagerAdapter(this@MainActivity).apply {
                addFragment(HomeFragment())
                addFragment(ExploreFragment())
                addFragment(MemoriesFragment())
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> b.mainActivityBottomBar.selectedItemId = R.id.bottom_bar_home
                        1 -> b.mainActivityBottomBar.selectedItemId = R.id.bottom_bar_explore
                        2 -> b.mainActivityBottomBar.selectedItemId = R.id.bottom_bar_memories
                        else -> b.mainActivityBottomBar.selectedItemId = R.id.bottom_bar_home
                    }
                    super.onPageSelected(position)
                }
            })
        }

        /**Set up a listener to update the ViewPager indices when the user switches tabs using the bottom navigation bar.*/
        b.mainActivityBottomBar.apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottom_bar_home -> b.mainActivityPager.currentItem = 0
                    R.id.bottom_bar_explore -> b.mainActivityPager.currentItem = 1
                    R.id.bottom_bar_memories -> b.mainActivityPager.currentItem = 2
                }
                true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the ID of the selected menu item
        val selectedItemId = b.mainActivityBottomBar.selectedItemId

        outState.putBoolean(INSTANCE_POPULATED, true)
        outState.putInt(INSTANCE_BOTTOM_BAR_SELECTED_ITEM, selectedItemId)
        super.onSaveInstanceState(outState)
    }

    companion object {
        //keys for handling savedInstanceState
        const val INSTANCE_POPULATED = "instance_populated"
        const val INSTANCE_BOTTOM_BAR_SELECTED_ITEM = "instance_bottom_bar_selected_item"
    }
}