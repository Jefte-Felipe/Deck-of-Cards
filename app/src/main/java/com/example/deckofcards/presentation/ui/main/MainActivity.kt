package com.example.deckofcards.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deckofcards.databinding.ActivityMainBinding
import com.example.deckofcards.presentation.ui.adapter.CardsAdapter
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

        viewModel.cards.observe(this) { cards ->
            adapter.submitList(cards)
        }

        binding.reshuffleButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deckId.value?.let { deckId ->
                    viewModel.reshuffleDeck(deckId)
                }
            }
        }
    }
}
