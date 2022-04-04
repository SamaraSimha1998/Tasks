package com.example.tasks.webretrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.databinding.ActivityWebApiRetrofitBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebApiRetrofit : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var binding: ActivityWebApiRetrofitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebApiRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("lifecycle", "onCreate invoked")

        binding.btnRxjava.setOnClickListener{ startRStream() }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerAdapter = RecyclerAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter

        //we use this phase when ever we give url to BaseUrl in ApiInterface.kt

        val apiInterface = ApiInterface.create().getMovies()

        apiInterface.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>?, response: Response<List<Movie>>?) {

                if (response?.body() != null)
                    recyclerAdapter.setMovieListItems(response.body()!!)
            }

            override fun onFailure(call: Call<List<Movie>>?, t: Throwable?) {

            }
        })


    }

    // Activity LifeCycle...
    override fun onStart() {
        super.onStart()
        Log.d("lifecycle", "onStart invoked")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "onResume invoked")
    }

    override fun onPause() {
        super.onPause()
        Log.d("lifecycle", "onPause invoked")
    }

    override fun onStop() {
        super.onStop()
        Log.d("lifecycle", "onStop invoked")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "onRestart invoked")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle", "onDestroy invoked")
    }

    //RxJava functionality starts from here...
    //startRStream() and getObserver()

    private fun startRStream() {

        val myObservable = getObservable()

        val myObserver = getObserver()

        myObservable
            .subscribe(myObserver)
    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(s: String) {
//                Log.d(tag, "onNext: $s")
            }

            override fun onError(e: Throwable) {
//                Log.e(tag, "onError: " + e.message)
            }

            override fun onComplete() {
//                Log.d(tag, "onComplete")
            }
        }
    }

    private fun getObservable(): Observable<String> {
        return Observable.just("1", "2", "3", "4", "5")
    }
}