package ie.wit.tritrack.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import ie.wit.tritrack.fragments.AddExerciseLogFragment
import ie.wit.tritrack.fragments.ExerciseLogListingFragment
import ie.wit.tritrack.R

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AddExerciseLogFragment(), "").commit()
        toolbar.setTitle("Add Exercise Log")

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        toolbar.setTitle("Add Exercise Log")


        navView.setNavigationItemSelectedListener({ menuItem ->
            applyMenuSelection(menuItem.itemId)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        applyMenuSelection(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    fun applyMenuSelection(id:Int){
        if (id == R.id.nav_add_exercise) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddExerciseLogFragment()).commit()
            toolbar.setTitle("Add Exercise Log")
            navView.setCheckedItem(R.id.nav_add_exercise)
        }
        if (id == R.id.nav_exercise_list) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ExerciseLogListingFragment()).commit()
            toolbar.setTitle("Exercise Log List")
            navView.setCheckedItem(R.id.nav_exercise_list)
        }
    }
    // override the onBackPressed() function to close the Drawer when the back button is clicked
    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}