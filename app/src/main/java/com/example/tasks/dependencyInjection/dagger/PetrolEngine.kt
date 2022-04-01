package com.example.tasks.dependencyInjection.dagger

import javax.inject.Inject
import javax.inject.Named

class PetrolEngine: Engine {

    var powerCapacity: Int
    var engineCapacity: Int

    // To use different Int values to parameters, we use @Named() annotation
    @Inject
    constructor(@Named("power")powerCapacity: Int, @Named("engine")engineCapacity: Int) {
        this.powerCapacity = powerCapacity
        this.engineCapacity = engineCapacity
    }

    override fun start() {
        println("Petrol engine started: Power Capacity = $powerCapacity \b Engine Capacity = $engineCapacity")
    }
}