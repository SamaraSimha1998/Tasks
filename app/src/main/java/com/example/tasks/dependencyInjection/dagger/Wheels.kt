package com.example.tasks.dependencyInjection.dagger

class Wheels {

    var rims: Rims
    var tires: Tires

    constructor(rims: Rims, tires: Tires) {
        this.rims = rims
        this.tires = tires
    }
}