package com.example.materialtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.materialtest.databinding.ActivityMainBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    val fruits = mutableListOf(Fruit("Orange", R.drawable.orange),

        Fruit("Banana", R.drawable.banana),
        Fruit("Grape", R.drawable.grape),
        Fruit("Blueberry", R.drawable.blueberry),
        Fruit("Cherry", R.drawable.cherry),
        Fruit("Lemon", R.drawable.lemon))

    val fruitList = ArrayList<Fruit>()

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = findViewById(R.id.drawerlayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size)
        }
        binding.navView.setCheckedItem(R.id.navCall)
        binding.navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            true
        }
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Data deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo") {
                    Toast.makeText(this, "Data restored", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
        initFruits()
        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = FruitAdapter(this, fruitList)
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.setColorSchemeResources(com.google.android.material.R.color.material_dynamic_primary10)
        binding.swipeRefresh.setOnRefreshListener {
            refreshFruits(adapter)
        }
    }

    private fun refreshFruits(adapter: FruitAdapter) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun initFruits() {
        fruitList.clear()
        repeat(50) {
            val index = (0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.boat -> Toast.makeText(this, "Boat clicked", Toast.LENGTH_SHORT).show()
            R.id.bus -> Toast.makeText(this, "Bus clicked", Toast.LENGTH_SHORT).show()
            R.id.car -> Toast.makeText(this, "Car clicked", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}