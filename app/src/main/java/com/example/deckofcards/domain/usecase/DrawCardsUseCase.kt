package com.example.deckofcards.domain.usecase

import com.example.deckofcards.data.repository.DeckRepository

class DrawCardsUseCase(private val repository: DeckRepository) {
    suspend fun execute(deckId: String, count: Int) = repository.drawCards(deckId, count)
}
