package com.example.deckofcards.domain.usecase

import com.example.deckofcards.data.model.ShuffleResponse
import com.example.deckofcards.data.repository.DeckRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

class ShuffleDeckUseCaseTest {
    private lateinit var repository: DeckRepository
    private lateinit var useCase: ShuffleDeckUseCase

    @Before
    fun setup() {
        repository = mock(DeckRepository::class.java)
        useCase = ShuffleDeckUseCase(repository)
    }

    @Test
    fun `execute should return success response when repository returns success`() {
        runBlocking {
            val response = ShuffleResponse(true, "deck_id", true, 52)
            `when`(repository.shuffleDeck(1)).thenReturn(Response.success(response))

            val result = useCase.execute(1)

            assertTrue(result.isSuccessful)
            assertEquals("deck_id", result.body()?.deck_id)
            verify(repository).shuffleDeck(1)
        }
    }
}
