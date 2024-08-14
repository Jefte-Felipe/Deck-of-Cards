package com.example.deckofcards.data.model

data class ShuffleResponse(
    val success: Boolean,
    val deck_id: String,
    val shuffled: Boolean,
    val remaining: Int
)