package com.example.deckofcards.data.repository

import com.example.deckofcards.data.api.DeckOfCardsApi
import com.example.deckofcards.data.model.DrawResponse
import com.example.deckofcards.data.model.ShuffleResponse
import retrofit2.Response

class DeckRepository(private val api: DeckOfCardsApi) {

    suspend fun shuffleDeck(deckCount: Int): Response<ShuffleResponse> {
        return api.shuffleDeck(deckCount)
    }

    suspend fun drawCards(deckId: String, count: Int): Response<DrawResponse> {
        return api.drawCards(deckId, count)
    }

    suspend fun reshuffleDeck(deckId: String): Response<ShuffleResponse> {
        return api.reshuffleDeck(deckId)
    }
}
