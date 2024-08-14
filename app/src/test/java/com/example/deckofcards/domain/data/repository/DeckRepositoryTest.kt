package com.example.deckofcards.domain.data.repository

import com.example.deckofcards.data.api.DeckOfCardsApi
import com.example.deckofcards.data.repository.DeckRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeckRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: DeckOfCardsApi
    private lateinit var repository: DeckRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api =
            Retrofit
                .Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DeckOfCardsApi::class.java)

        repository = DeckRepository(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `shuffleDeck should return success response`() =
        runBlocking {
            val mockResponse =
                MockResponse()
                    .setBody("""{"success":true,"deck_id":"deck_id","shuffled":true,"remaining":52}""")
                    .setResponseCode(200)
            mockWebServer.enqueue(mockResponse)

            val response = repository.shuffleDeck(1)

            assertTrue(response.isSuccessful)
            assertEquals("deck_id", response.body()?.deck_id)
        }
}
