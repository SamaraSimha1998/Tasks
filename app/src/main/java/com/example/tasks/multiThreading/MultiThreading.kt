package com.example.tasks.multiThreading

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasks.R
import kotlinx.android.synthetic.main.activity_multi_threading.*

class MultiThreading : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_threading)

        var bool
                = false
        btn_start.setOnClickListener{
            bool = !bool
            if (!bool)
            {
                thread_one.text = "Stopped1"
                thread_two.text = "Stopped2"
                thread_three.text = "Stopped3"
                btn_start.text = "Start"
            }
            else
            {
                btn_start.text = "Stop"

                Thread {
                    while (bool) {

                        runOnUiThread {
                            thread_one.text = "Started1"
                        }
                        Thread.sleep(1000)
                        runOnUiThread {
                            thread_one.text = "Activity1"
                        }
                        Thread.sleep(1000)
                    }
                }.start()

                Thread {
                    while (bool) {
                        runOnUiThread {
                            thread_two.text = "Started2"
                        }
                        Thread.sleep(1000)
                        runOnUiThread {
                            thread_two.text = "Activity2"
                        }
                        Thread.sleep(1000)
                    }
                }.start()

                Thread {
                    while (bool) {
                        runOnUiThread {
                            thread_three.text = "Started3"
                        }
                        Thread.sleep(1000)
                        runOnUiThread {
                            thread_three.text = "Activity3"
                        }
                        Thread.sleep(1000)
                    }
                }
                    .start()
            }
        }
    }
}