package com.example.deckofcards.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deckofcards.databinding.ActivityMainBinding
import com.example.deckofcards.presentation.ui.adapter.CardsAdapter
import com.example.deckofcards.presentation.ui.utils.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: CardsViewModel by viewModel()
    private lateinit var adapter: CardsAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CardsAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.cards.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.errorView.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.errorView.visibility = View.GONE
                    adapter.submitList(state.data)
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.errorView.visibility = View.VISIBLE
                    binding.errorView.text = state.message
                }
            }
        }

        binding.reshuffleButton.setOnClickListener {
            lifecycleScope.launch {
                when (val deckIdState = viewModel.deckId.value) {
                    is UiState.Success -> viewModel.reshuffleDeck(deckIdState.data)
                    else -> Unit
                }
            }
        }
    }
}
