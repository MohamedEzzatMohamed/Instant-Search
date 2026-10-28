package com.mezzat.instantsearch.search.di

import com.mezzat.instantsearch.BuildConfig
import com.mezzat.instantsearch.search.data.repository.SearchCharactersRepositoryImpl
import com.mezzat.instantsearch.search.domain.repository.SearchCharactersRepository
import com.mezzat.instantsearch.search.domain.usecase.SearchCharactersUseCase
import com.mezzat.instantsearch.search.presentation.viewmodel.SearchViewModel
import com.shared.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchModule = module {
    single<HttpClient> {
        HttpClientFactory.create(
            baseUrl = BuildConfig.BASE_URL
        )
    }

    singleOf(::SearchCharactersRepositoryImpl) bind SearchCharactersRepository::class
    factoryOf(::SearchCharactersUseCase)
    viewModelOf(::SearchViewModel)
}
