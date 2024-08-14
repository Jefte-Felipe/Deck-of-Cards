package com.example.deckofcards.domain.usecase

import com.example.deckofcards.data.repository.DeckRepository

class ShuffleDeckUseCase(private val repository: DeckRepository) {
    suspend fun execute(deckCount: Int) = repository.shuffleDeck(deckCount)
}
