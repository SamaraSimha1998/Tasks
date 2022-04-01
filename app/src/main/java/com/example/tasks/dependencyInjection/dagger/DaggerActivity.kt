package com.example.tasks.dependencyInjection.dagger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasks.R
import javax.inject.Inject

class DaggerActivity : AppCompatActivity() {

    @Inject
    lateinit var car: Car
    @Inject
    lateinit var car1: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger)

//        val carComponent = (application as ExampleApp).carComponent()

//        carComponent.inject(this)

        // @Singleton method test
        val carComponent = DaggerCarComponent.builder()
            .powerCapacity(300)
            .engineCapacity(1000)
            .build()
        carComponent.inject(this)

        val carComponent1 = DaggerCarComponent.builder()
            .powerCapacity(300)
            .engineCapacity(1000)
            .build()
         carComponent1.inject(this)
        car = carComponent.getCar()
        car.start()
        car1 = carComponent1.getCar()
        car1.start()
    }
}