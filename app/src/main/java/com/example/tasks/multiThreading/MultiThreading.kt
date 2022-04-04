package com.example.tasks.multiThreading

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasks.databinding.ActivityMultiThreadingBinding

class MultiThreading : AppCompatActivity() {

    private lateinit var binding: ActivityMultiThreadingBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiThreadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bool
                = false
        binding.btnStart.setOnClickListener{
            bool = !bool
            if (!bool)
            {
                binding.threadOne.text = "Stopped1"
                binding.threadTwo.text = "Stopped2"
                binding.threadThree.text = "Stopped3"
                binding.btnStart.text = "Start"
            }
            else
            {
                binding.btnStart.text = "Stop"

                // This way we run different threads
                Thread {
                    while (bool) {

                        runOnUiThread {
                            binding.threadOne.text = "Started1"
                        }
                        Thread.sleep(1000)
                        runOnUiThread {
                            binding.threadOne.text = "Activity1"
                        }
                        Thread.sleep(1000)
                    }
                }.start()

                Thread {
                    while (bool) {
                        runOnUiThread {
                            binding.threadTwo.text = "Started2"
                        }
                        Thread.sleep(1000)
                        runOnUiThread {
                            binding.threadTwo.text = "Activity2"
                        }
                        Thread.sleep(1000)
                    }
                }.start()

                Thread {
                    while (bool) {
                        runOnUiThread {
                            binding.threadThree.text = "Started3"
                        }
                        Thread.sleep(1000)
                        runOnUiThread {
                            binding.threadThree.text = "Activity3"
                        }
                        Thread.sleep(1000)
                    }
                }
                    .start()
            }
        }
    }
}