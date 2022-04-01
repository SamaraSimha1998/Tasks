package com.example.tasks.dependencyInjection.dagger

import javax.inject.Inject

class Car {

    // Passing runtime values to the dependent objects
    @Inject
    lateinit var wheels: Wheels
    var engine: Engine
    var driver: Driver

    @Inject
    constructor(engine: Engine, driver: Driver) {
        this.engine = engine
        this.driver = driver
    }

    // This argument 'remote' should be created by dagger, So we use inject.
    @Inject
    fun provideCarToRemote(remote: Remote) {
        remote.provideCar(this)
    }
    fun start() {
        println("Driver: $driver")
        println("wheels: $wheels")
        engine.start()
        println("driving...")
    }
}