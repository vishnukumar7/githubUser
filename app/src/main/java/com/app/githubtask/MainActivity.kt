package com.app.githubtask

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.githubtask.databinding.ActivityMainBinding
import com.app.githubtask.db.DatabaseClient
import com.app.githubtask.ui.DetailFragment
import com.app.githubtask.ui.HomeFragment
import com.app.githubtask.ui.NotificationsFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
   lateinit var binding: ActivityMainBinding
   lateinit var content: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        loadFragment(HomeFragment.getInstance())
        binding.bottomNavigation.setOnItemSelectedListener(this)
    }

    val userViewModel: UserViewModel by viewModels {
        UserViewModel.UserViewModelFactory(appRepository)
    }

    private val database by lazy { DatabaseClient.getInstance(this) }

    private val appRepository by lazy { AppRepository(database.userDao(),ApiClient.getAPI()) }

    fun loadFragment(fragment: Fragment) {
        content = fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not be
     * selected. Consider setting non-selectable items as disabled preemptively to make them
     * appear non-interactive.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> if (content !is HomeFragment) loadFragment(HomeFragment.getInstance())
            R.id.navigation_dashboard -> if (content !is DetailFragment) loadFragment(DetailFragment.getInstance())
            R.id.navigation_notifications -> if (content !is NotificationsFragment) loadFragment(
                NotificationsFragment.getInstance()
            )
        }
        return true
    }

    override fun onBackPressed() {
        if (content !is HomeFragment) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_home
            loadFragment(HomeFragment.getInstance())
        } else {
            finish()
        }
    }
}