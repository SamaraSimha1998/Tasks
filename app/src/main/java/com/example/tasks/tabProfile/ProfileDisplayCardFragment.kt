package com.example.tasks.tabProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile_display_card.view.*


class ProfileDisplayCardFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var recview: RecyclerView? = null
    private var adapter: Adapter? = null
    private var listAdapter: Array<Adapter?> = arrayOf(adapter)
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_profile_display_card, container, false)
        recview = view.findViewById<View>(R.id.profile_card_recycler_view) as RecyclerView
        recview!!.layoutManager = LinearLayoutManager(context)
        val options: FirebaseRecyclerOptions<Model> = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("Model"), Model::class.java)
            .build()
        adapter = Adapter(options)
        recview!!.adapter = adapter

        database = FirebaseDatabase.getInstance().reference.child("Model")
        database.keepSynced(true)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childDataSnapshot in dataSnapshot.children) {
                    if (childDataSnapshot.child("Model").value != null) {
                        val data: ArrayList<String?> = ArrayList()
                        for (ing in childDataSnapshot.child("Model").children) {
                            data.add(ing.child("Model").getValue(Model::class.java).toString())
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        
        view.profile_card_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(position: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(position: String?): Boolean {
                listAdapter.filter(position)
                return false
            }

        })

        return view
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): ProfileDisplayCardFragment {
            val fragment = ProfileDisplayCardFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}

private fun <T> Array<T>.filter(predicate: String?) {

}
