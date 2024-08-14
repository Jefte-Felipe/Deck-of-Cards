package com.example.deckofcards.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deckofcards.data.model.Card
import com.example.deckofcards.domain.usecase.DrawCardsUseCase
import com.example.deckofcards.domain.usecase.ReshuffleDeckUseCase
import com.example.deckofcards.domain.usecase.ShuffleDeckUseCase
import com.example.deckofcards.presentation.ui.utils.UiState
import kotlinx.coroutines.launch

class CardsViewModel(
    private val shuffleDeckUseCase: ShuffleDeckUseCase,
    private val drawCardsUseCase: DrawCardsUseCase,
    private val reshuffleDeckUseCase: ReshuffleDeckUseCase,
) : ViewModel() {
    private val _cards = MutableLiveData<UiState<List<Card>>>()
    val cards: LiveData<UiState<List<Card>>> = _cards

    private val _deckId = MutableLiveData<UiState<String>>()
    val deckId: LiveData<UiState<String>> = _deckId

    init {
        shuffleDeck(1)
    }

    fun shuffleDeck(deckCount: Int) {
        _deckId.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = shuffleDeckUseCase.execute(deckCount)
                if (response.isSuccessful) {
                    val deckId = response.body()?.deck_id.orEmpty()
                    _deckId.value = UiState.Success(deckId)
                    drawCards(deckId, 2)
                } else {
                    _deckId.value = UiState.Error("Failed to shuffle deck")
                }
            } catch (e: Exception) {
                _deckId.value = UiState.Error("Exception occurred", e)
            }
        }
    }

    fun drawCards(
        deckId: String,
        count: Int,
    ) {
        _cards.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = drawCardsUseCase.execute(deckId, count)
                if (response.isSuccessful) {
                    _cards.value = UiState.Success(response.body()?.cards.orEmpty())
                } else {
                    _cards.value = UiState.Error("Failed to draw cards")
                }
            } catch (e: Exception) {
                _cards.value = UiState.Error("Exception occurred", e)
            }
        }
    }

    fun reshuffleDeck(deckId: String) {
        _deckId.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = reshuffleDeckUseCase.execute(deckId)
                if (response.isSuccessful) {
                    val newDeckId = response.body()?.deck_id.orEmpty()
                    _deckId.value = UiState.Success(newDeckId)
                    drawCards(newDeckId, 2)
                } else {
                    _deckId.value = UiState.Error("Failed to reshuffle deck")
                }
            } catch (e: Exception) {
                _deckId.value = UiState.Error("Exception occurred", e)
            }
        }
    }
}
