package com.example.tasks.dependencyInjection.dagger

import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [PetrolEngineModule::class, WheelsModule::class])
interface CarComponent {
    fun getCar(): Car

    fun inject(daggerActivity: DaggerActivity)

    @Component.Builder
    interface Builder {
        // Method to pass runtime values
        // To use different Int values to parameters, we use @Named() annotation
        @BindsInstance
        fun powerCapacity(@Named("power")powerCapacity: Int): Builder

        @BindsInstance
        fun engineCapacity(@Named("engine")engineCapacity: Int): Builder

        // To get car component instance
        fun build(): CarComponent
    }
}