package com.example.tasks.dependencyInjection.dagger

import dagger.Module
import dagger.Provides

@Module
class WheelsModule {

    @Provides
    fun providesRims(): Rims {
        println("Rims created")
        return Rims()
    }

    @Provides
    fun providesTires(): Tires {
        println("Tires created")
        return Tires()
    }

    @Provides
    fun providesWheels(rims: Rims, tires: Tires): Wheels {
        println("Wheels module")
        return Wheels(rims, tires)
    }
}