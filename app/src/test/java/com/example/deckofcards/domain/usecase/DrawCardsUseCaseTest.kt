package com.example.deckofcards.domain.usecase

import com.example.deckofcards.data.model.Card
import com.example.deckofcards.data.model.DrawResponse
import com.example.deckofcards.data.repository.DeckRepository
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

class DrawCardsUseCaseTest {
    private lateinit var repository: DeckRepository
    private lateinit var useCase: DrawCardsUseCase

    @Before
    fun setup() {
        repository = mock(DeckRepository::class.java)
        useCase = DrawCardsUseCase(repository)
    }

    @Test
    fun `execute should return success response when repository returns success`() =
        runBlocking {
            // Arrange
            val deckId = "deck_id"
            val count = 2
            val cards = listOf(Card("6H", "https://deckofcardsapi.com/static/img/6H.png", "6", "HEARTS"))
            val expectedResponse = Response.success(DrawResponse(true, deckId, cards, 50))
            `when`(repository.drawCards(deckId, count)).thenReturn(expectedResponse)

            val result = useCase.execute(deckId, count)

            assertEquals(expectedResponse, result)
        }

    @Test
    fun `execute should return error response when repository returns error`() =
        runBlocking {
            val deckId = "deck_id"
            val count = 2
            val expectedResponse = Response.error<DrawResponse>(500, ResponseBody.create(null, ""))
            `when`(repository.drawCards(deckId, count)).thenReturn(expectedResponse)

            val result = useCase.execute(deckId, count)

            assertEquals(expectedResponse, result)
        }
}
