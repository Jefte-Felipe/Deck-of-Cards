package com.example.deckofcards.di

import com.example.deckofcards.data.api.DeckOfCardsApi
import com.example.deckofcards.data.repository.DeckRepository
import com.example.deckofcards.domain.usecase.DrawCardsUseCase
import com.example.deckofcards.domain.usecase.ReshuffleDeckUseCase
import com.example.deckofcards.domain.usecase.ShuffleDeckUseCase
import com.example.deckofcards.presentation.ui.main.CardsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://deckofcardsapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(DeckOfCardsApi::class.java) }

    single { DeckRepository(get()) }

    single { ShuffleDeckUseCase(get()) }
    single { DrawCardsUseCase(get()) }
    single { ReshuffleDeckUseCase(get()) }

    viewModel { CardsViewModel(get(), get(), get()) }
}
