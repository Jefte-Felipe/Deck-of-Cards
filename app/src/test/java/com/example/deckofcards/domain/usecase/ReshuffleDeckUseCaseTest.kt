package com.example.deckofcards.domain.usecase

import com.example.deckofcards.data.model.ShuffleResponse
import com.example.deckofcards.data.repository.DeckRepository
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class ReshuffleDeckUseCaseTest {
    private lateinit var repository: DeckRepository
    private lateinit var useCase: ReshuffleDeckUseCase

    @Before
    fun setup() {
        repository = mock(DeckRepository::class.java)
        useCase = ReshuffleDeckUseCase(repository)
    }

    @Test
    fun `execute should return success response when repository returns success`() =
        runBlocking {
            val deckId = "deck_id"
            val expectedResponse = Response.success(ShuffleResponse(true, deckId, true, 52))
            `when`(repository.reshuffleDeck(deckId)).thenReturn(expectedResponse)

            val result = useCase.execute(deckId)

            assertEquals(expectedResponse, result)
        }

    @Test
    fun `execute should return error response when repository returns error`() =
        runBlocking {
            val deckId = "deck_id"
            val expectedResponse = Response.error<ShuffleResponse>(500, ResponseBody.create(null, ""))
            `when`(repository.reshuffleDeck(deckId)).thenReturn(expectedResponse)

            val result = useCase.execute(deckId)

            assertEquals(expectedResponse, result)
        }
}
