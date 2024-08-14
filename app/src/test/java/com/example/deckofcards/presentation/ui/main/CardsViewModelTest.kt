package com.example.deckofcards.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.deckofcards.data.model.Card
import com.example.deckofcards.data.model.DrawResponse
import com.example.deckofcards.data.model.ShuffleResponse
import com.example.deckofcards.domain.usecase.DrawCardsUseCase
import com.example.deckofcards.domain.usecase.ReshuffleDeckUseCase
import com.example.deckofcards.domain.usecase.ShuffleDeckUseCase
import com.example.deckofcards.presentation.ui.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CardsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CardsViewModel
    private lateinit var shuffleDeckUseCase: ShuffleDeckUseCase
    private lateinit var drawCardsUseCase: DrawCardsUseCase
    private lateinit var reshuffleDeckUseCase: ReshuffleDeckUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        shuffleDeckUseCase = mock(ShuffleDeckUseCase::class.java)
        drawCardsUseCase = mock(DrawCardsUseCase::class.java)
        reshuffleDeckUseCase = mock(ReshuffleDeckUseCase::class.java)
        viewModel = CardsViewModel(shuffleDeckUseCase, drawCardsUseCase, reshuffleDeckUseCase)
    }

    @Test
    fun `when shuffleDeck is successful, deckId should be updated`() =
        runTest {
            val shuffleResponse = Response.success(ShuffleResponse(true, "deck_id", true, 52))
            `when`(shuffleDeckUseCase.execute(1)).thenReturn(shuffleResponse)

            viewModel.shuffleDeck(1)

            assertTrue(viewModel.deckId.value is UiState.Success)
            assertEquals("deck_id", (viewModel.deckId.value as UiState.Success).data)
        }

    @Test
    fun `when drawCards is successful, cards should be updated`() =
        runTest(UnconfinedTestDispatcher()) {
            val deckId = "deck_id"
            val cards = listOf(Card("6H", "https://deckofcardsapi.com/static/img/6H.png", "6", "HEARTS"))
            val drawResponse = Response.success(DrawResponse(true, deckId, cards, 50))
            `when`(drawCardsUseCase.execute(deckId, 2)).thenReturn(drawResponse)

            viewModel.drawCards(deckId, 2)

            assertTrue(viewModel.cards.value is UiState.Success)
            assertEquals(cards, (viewModel.cards.value as UiState.Success).data)
        }

    @Test
    fun `when reshuffleDeck is successful, deckId and cards should be updated`() =
        runTest(UnconfinedTestDispatcher()) {
            val deckId = "deck_id"
            val newDeckId = "new_deck_id"
            val cards = listOf(Card("6H", "https://deckofcardsapi.com/static/img/6H.png", "6", "HEARTS"))

            val reshuffleResponse = Response.success(ShuffleResponse(true, newDeckId, true, 52))
            val drawResponse = Response.success(DrawResponse(true, newDeckId, cards, 50))

            `when`(reshuffleDeckUseCase.execute(deckId)).thenReturn(reshuffleResponse)
            `when`(drawCardsUseCase.execute(newDeckId, 2)).thenReturn(drawResponse)

            viewModel.reshuffleDeck(deckId)

            assertTrue(viewModel.deckId.value is UiState.Success)
            assertEquals(newDeckId, (viewModel.deckId.value as UiState.Success).data)
            assertTrue(viewModel.cards.value is UiState.Success)
            assertEquals(cards, (viewModel.cards.value as UiState.Success).data)
        }

    @Test
    fun `when shuffleDeck fails, deckId should have error`() =
        runTest(UnconfinedTestDispatcher()) {
            `when`(shuffleDeckUseCase.execute(1)).thenReturn(Response.error(500, ResponseBody.create(null, "")))

            viewModel.shuffleDeck(1)

            assertTrue(viewModel.deckId.value is UiState.Error)
            assertEquals("Failed to shuffle deck", (viewModel.deckId.value as UiState.Error).message)
        }

    @Test
    fun `when drawCards fails, cards should have error`() =
        runTest(UnconfinedTestDispatcher()) {
            val deckId = "deck_id"
            `when`(drawCardsUseCase.execute(deckId, 2)).thenReturn(Response.error(500, ResponseBody.create(null, "")))

            viewModel.drawCards(deckId, 2)

            assertTrue(viewModel.cards.value is UiState.Error)
            assertEquals("Failed to draw cards", (viewModel.cards.value as UiState.Error).message)
        }
}
