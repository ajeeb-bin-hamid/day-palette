package com.day.palette.presentation.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.day.palette.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

        vm.observe(this, state = ::observeState)
        observeIntent()

        b.mainActivityTV.setOnClickListener {
            vm.vmActions(MainIntent.FetchData)
        }

    }

    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: MainState) {
        println("infox, ${state.isLoading}")
    }

    /**Observe for new intents using SharedFlow*/
    private fun observeIntent() {
        lifecycleScope.launch {
            vm.uiActions.collect { intent ->
                when (intent) {
                    is MainIntent.ShowToast -> {
                        Toast.makeText(this@MainActivity, intent.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        //
                    }
                }
            }
        }
    }

}