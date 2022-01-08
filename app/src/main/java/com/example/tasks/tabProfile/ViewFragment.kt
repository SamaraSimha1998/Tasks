package com.example.tasks.tabProfile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.tasks.R
import java.util.*


class ViewFragment : Fragment {
    private var mParam1: String? = null
    private var mParam2: String? = null
    var firstName: String? = null
    private var image: String? = null
    var lastName: String? = null
    var phone: String? = null
    private lateinit var baseImage : String

    constructor() {}
    constructor(firstName: String?, image: String?, lastName: String?, phone: String?) {
        this.firstName = firstName
        this.image = image
        this.lastName = lastName
        this.phone = phone
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_descfragment, container, false)
        val imageHolder = view.findViewById<ImageView>(R.id.image_holder)
        val firstNameHolder = view.findViewById<TextView>(R.id.first_name_holder)
        val phoneHolder = view.findViewById<TextView>(R.id.phone_holder)
        val lastNameHolder = view.findViewById<TextView>(R.id.last_name_holder)
        firstNameHolder.text = firstName
        phoneHolder.text = phone
        lastNameHolder.text = lastName
        baseImage = image.toString()
        Glide.with(requireContext()).load(base64ToBitmap(baseImage)).into(imageHolder)
        return view
    }

    fun onBackPressed() {
        val activity = context as AppCompatActivity?
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.profile_view_pager, ProfileDisplayCardFragment())
            .addToBackStack(null).commit()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): ViewFragment {
            val fragment = ViewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun base64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.getDecoder().decode(b64)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }
}