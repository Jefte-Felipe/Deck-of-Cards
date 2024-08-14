package com.example.deckofcards.domain.usecase

import com.example.deckofcards.data.repository.DeckRepository

class ReshuffleDeckUseCase(private val repository: DeckRepository) {
    suspend fun execute(deckId: String) = repository.reshuffleDeck(deckId)
}