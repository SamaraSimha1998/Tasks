package com.example.tasks.dependencyInjection.dagger

import javax.inject.Inject

class DieselEngine: Engine {

    @Inject
    constructor() {}

    override fun start() {
        println("Diesel engine started")
    }
}