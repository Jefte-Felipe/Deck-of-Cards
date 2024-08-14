package com.example.deckofcards.data.api

import com.example.deckofcards.data.model.DrawResponse
import com.example.deckofcards.data.model.ShuffleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeckOfCardsApi {
    @GET("deck/new/shuffle/")
    suspend fun shuffleDeck(
        @Query("deck_count") deckCount: Int = 1
    ): Response<ShuffleResponse>

    @GET("deck/{deck_id}/draw/")
    suspend fun drawCards(
        @Path("deck_id") deckId: String,
        @Query("count") count: Int
    ): Response<DrawResponse>

    @GET("deck/{deck_id}/shuffle/")
    suspend fun reshuffleDeck(
        @Path("deck_id") deckId: String
    ): Response<ShuffleResponse>
}
