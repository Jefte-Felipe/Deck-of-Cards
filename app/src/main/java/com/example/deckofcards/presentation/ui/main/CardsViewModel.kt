package com.example.deckofcards.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deckofcards.data.model.Card
import com.example.deckofcards.domain.usecase.DrawCardsUseCase
import com.example.deckofcards.domain.usecase.ReshuffleDeckUseCase
import com.example.deckofcards.domain.usecase.ShuffleDeckUseCase
import kotlinx.coroutines.launch

class CardsViewModel(
    private val shuffleDeckUseCase: ShuffleDeckUseCase,
    private val drawCardsUseCase: DrawCardsUseCase,
    private val reshuffleDeckUseCase: ReshuffleDeckUseCase,
) : ViewModel() {
    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    private val _deckId = MutableLiveData<String>()
    val deckId: LiveData<String> = _deckId

    init {
        shuffleDeck(1)
    }

    private fun shuffleDeck(deckCount: Int) {
        viewModelScope.launch {
            val response = shuffleDeckUseCase.execute(deckCount)
            if (response.isSuccessful) {
                _deckId.value = response.body()?.deck_id
                drawCards(response.body()?.deck_id ?: "", 2)
            }
        }
    }

    private fun drawCards(
        deckId: String,
        count: Int,
    ) {
        viewModelScope.launch {
            val response = drawCardsUseCase.execute(deckId, count)
            if (response.isSuccessful) {
                _cards.value = response.body()?.cards
            }
        }
    }

    fun reshuffleDeck(deckId: String) {
        viewModelScope.launch {
            val response = reshuffleDeckUseCase.execute(deckId)
            if (response.isSuccessful) {
                _deckId.value = response.body()?.deck_id
                drawCards(deckId, 2)
            }
        }
    }
}
