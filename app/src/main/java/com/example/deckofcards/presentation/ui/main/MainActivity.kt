package com.example.deckofcards.presentation.ui.main

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deckofcards.R
import com.example.deckofcards.presentation.ui.adapter.CardsAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: CardsViewModel by viewModel()
    private lateinit var adapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = CardsAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.cards.observe(
            this,
            Observer { cards ->
                adapter.submitList(cards)
            },
        )

        findViewById<Button>(R.id.reshuffleButton).setOnClickListener {
            lifecycleScope.launch {
                viewModel.deckId.value?.let { deckId ->
                    viewModel.reshuffleDeck(deckId)
                }
            }
        }
    }
}
