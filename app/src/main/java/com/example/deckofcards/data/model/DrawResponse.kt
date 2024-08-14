package com.example.deckofcards.data.model

data class DrawResponse(
    val success: Boolean,
    val deck_id: String,
    val cards: List<Card>,
    val remaining: Int
)