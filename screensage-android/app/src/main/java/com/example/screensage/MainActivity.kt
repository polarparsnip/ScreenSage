package com.example.screensage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.screensage.Util.ToastUtil
import com.example.screensage.databinding.ActivityMainBinding
import com.example.screensage.service.AuthManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super. onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val publicFragments = setOf(R.id.loginFragment, R.id.registerFragment)

            if (!AuthManager.isLoggedIn(this) && destination.id !in publicFragments) {
                navController.navigate(R.id.loginFragment)
                return@addOnDestinationChangedListener
            }

            if (destination.id in publicFragments) {
                disableNavigationUI()
            } else {
                setupNavigationUI(navController)
            }
        }

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Random action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
    }

    private fun setupNavigationUI(navController: NavController) {
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        binding.appBarMain.toolbar.visibility = android.view.View.VISIBLE
        navView.visibility = android.view.View.VISIBLE

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun disableNavigationUI() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.appBarMain.toolbar.visibility = android.view.View.GONE
        binding.navView.visibility = android.view.View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_gallery)
                true
            }
            R.id.action_logout -> {
                AuthManager.removeToken(this)
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.loginFragment)
                ToastUtil.showToast(this, "Logged out successfully.")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}