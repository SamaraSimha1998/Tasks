package com.example.tasks.dependencyInjection.dagger

import dagger.Module
import dagger.Provides

@Module
class DieselEngineModule {

    @Provides
    fun providesDieselEngine(dieselEngine: DieselEngine) : Engine {
        return dieselEngine
    }
}