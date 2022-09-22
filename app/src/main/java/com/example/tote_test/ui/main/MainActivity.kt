package com.example.tote_test.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.tote_test.R
import com.example.tote_test.databinding.ActivityMainBinding
import com.example.tote_test.utils.*
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            //if (f is TabsFragment || f is NavHostFragment) return

            if (f is NavHostFragment) return

            onNavControllerActivated()
        }
    }

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        toLog("destinationListener: $destination")
        supportActionBar?.title = destination.label
        //supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        APP_ACTIVITY = this

        /*lifecycleScope.launch {
            REPOSITORY.getGambler()
            toLog("GAMBLER: $GAMBLER")
        }*/

        /*if (AppPreferences.getIsAuth()) {
            toLog("MainActivity -> getGambler before: $GAMBLER")
            *//*viewModel.getGambler {
                toLog("MainActivity -> getGambler after: $GAMBLER")
                //viewModel.changeGambler(GAMBLER)
            }*//*
            lifecycleScope.launch {

                REPOSITORY.getGambler()
                prepareRootNavController(navController)
                onNavControllerActivated(navController)
            }
        }*/

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setCopyright()

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        navController = (supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment).navController
        val graph = navController.navInflater.inflate(R.navigation.main_graph)
        graph.setStartDestination(R.id.startFragment)
        navController.graph = graph

    }

    private fun onNavControllerActivated() {
        navController.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
    }

    private fun setCopyright() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val copyright = binding.mainFooter.copyrightYear

        if (year != YEAR_START) {
            val strYear = "$YEAR_START-$year"
            copyright.text = strYear
        } else {
            copyright.text = YEAR_START.toString()
        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onDestroy()
    }
}