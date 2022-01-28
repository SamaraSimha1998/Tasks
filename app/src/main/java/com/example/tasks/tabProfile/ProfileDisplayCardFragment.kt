package com.example.tasks.tabProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile_display_card.view.*
import java.io.File
import java.io.FileWriter

class ProfileDisplayCardFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var recyclerView: RecyclerView? = null
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
        view.btn_save_to_text_file.setOnClickListener { download() }
        view.btn_share.setOnClickListener { share() }
        recyclerView = view.findViewById<View>(R.id.profile_card_recycler_view) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        val options: FirebaseRecyclerOptions<Model> = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("Model"), Model::class.java)
            .build()
        adapter = Adapter(options)
        recyclerView!!.adapter = adapter

        database = FirebaseDatabase.getInstance().reference.child("Model")

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

    private fun download() {
        /**
        try {
            val storage = FirebaseDatabase.getInstance()
            val storageRef = storage.getReferenceFromUrl("https://tasks-1b864-default-rtdb.firebaseio.com/")
                .child("Model")
            val localFile: File = File.createTempFile("localData", "txt")
            storageRef.setValue(localFile.absoluteFile)
                .addOnSuccessListener {
                    val downloadManager =
                        getSystemService() as DownloadManager?
                    val uri = Uri.parse("localData")

                    val request = DownloadManager.Request(uri)
                    request.setTitle("My File")
                    request.setDescription("Downloading")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setVisibleInDownloadsUi(false)
                    request.setDestinationUri(Uri.parse("file://localData.txt"))

                    downloadManager!!.enqueue(request)
                    Toast.makeText(activity,"File Downloaded",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "File Not Downloaded", Toast.LENGTH_SHORT).show()
                }
        } catch (e: IOException) {
        }

        val localFile: File = File.createTempFile("file", "txt")
        database = FirebaseDatabase.getInstance().reference.child("Model")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childDataSnapshot in dataSnapshot.children) {
                    localFile.writeText(childDataSnapshot.child("Model").getValue(Model::class.java).toString())

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        **/

        // Creates data snapshot from Model data class
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

        try {
            // Creates a file in external storage to save data
            val fileName="localData.txt"
            val addData = FileWriter(File("sdcard/${fileName}"))
            addData.write(database.toString())
            addData.close()
            Toast.makeText(activity, "File saved successfully!", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Retrieves text file from storage and loads to share file to another applications
    private fun share() {
        val file = File(Environment.getExternalStorageDirectory(), "localData.txt")
        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("Email")
        val array = arrayOf("") // receiver id
        intent.putExtra(Intent.EXTRA_EMAIL,array)
        intent.putExtra(Intent.EXTRA_SUBJECT,"data")
        intent.putExtra(Intent.EXTRA_TEXT,"Select to share")
        intent.putExtra(Intent.EXTRA_STREAM, file)
        intent.type = "message/rfc822"
        val a = Intent.createChooser(intent,"Launch Email")
        startActivity(a)
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
