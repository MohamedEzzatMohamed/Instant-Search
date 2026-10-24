package com.mezzat.instantsearch

import android.app.Application
import com.mezzat.instantsearch.search.di.searchModule
import com.shared.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InstantSearchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@InstantSearchApp)
            modules(coreModule, searchModule)
        }
    }
}
