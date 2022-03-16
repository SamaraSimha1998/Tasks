package com.example.tasks.aapoonLoginPage.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.MainActivity
import com.example.tasks.R
import com.example.tasks.aapoonLoginPage.PhoneNumberVerification
import com.example.tasks.aapoonLoginPage.menuView.AppProfileViewActivity
import com.example.tasks.aapoonLoginPage.menuView.ReferralActivity
import com.example.tasks.aapoonLoginPage.menuView.SupportTeamActivity
import com.example.tasks.aapoonLoginPage.menuView.UpdateNumberActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.math.MathUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_profile_menu.*
import java.util.*
import kotlin.math.roundToInt

class DashBoardActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var userName : String
    private lateinit var baseImage : String
    private val sharedAppLoginNumber = "loginNumber"
    private var bottomSheetBehavior: BottomSheetBehavior<NavigationView>? = null
    private lateinit var phoneNumber: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        phoneNumber = intent.getStringExtra("phoneNumber").toString()

        initComponent()

        dashboardTabs()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComponent() {

        database = FirebaseDatabase.getInstance().getReference("AppProfiles")
        database.child(phoneNumber).get().addOnSuccessListener {
            when {
                it.exists() -> {
                    baseImage = it.child("image").value.toString()
                    val firstName = it.child("firstName").value.toString()
                    userName = firstName
                    app_user_profile_image_view.setImageBitmap(base64ToBitmap(baseImage))
                    app_user_profile_name_text_view.text = userName
                }
            }
        }

        val scrim = findViewById<FrameLayout>(R.id.scrim)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        setSupportActionBar(bottomAppBar)
        val navigationView = findViewById<NavigationView>(R.id.aapoon_menu_navigation)
        bottomAppBar.title = "Menu"
        bottomSheetBehavior = BottomSheetBehavior.from(navigationView)
        (bottomSheetBehavior as BottomSheetBehavior<*>).state = BottomSheetBehavior.STATE_HIDDEN
        bottomAppBar.setNavigationOnClickListener {
            if ((bottomSheetBehavior as BottomSheetBehavior<*>).state == BottomSheetBehavior.STATE_EXPANDED) {
                (bottomSheetBehavior as BottomSheetBehavior<*>).setState(BottomSheetBehavior.STATE_HIDDEN)
            } else {
                (bottomSheetBehavior as BottomSheetBehavior<*>).setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            (bottomSheetBehavior as BottomSheetBehavior<*>).state = BottomSheetBehavior.STATE_HIDDEN
            when (menuItem.title) {
                "My Profile" -> {
                    // views profile details
                    val intent = Intent(this, AppProfileViewActivity::class.java)
                    intent.putExtra("phoneNumber",phoneNumber)
                    startActivity(intent)
                }
                "Settings" -> {
                    // settings
                }
                "Refer Friends" -> {
                    // refer friends
                    val intent = Intent(this, ReferralActivity::class.java)
                    startActivity(intent)
                }
                "Update Phone Number" -> {
                    // update phone number
                    val intent = Intent(this, UpdateNumberActivity::class.java)
                    startActivity(intent)
                }
                "Support" -> {
                    // support
                    val intent = Intent(this, SupportTeamActivity::class.java)
                    startActivity(intent)
                }
                "Privacy Policy" -> {
                    // terms and conditions / privacy policies
                    showAlert()
                }
                "Logout" -> {
                    // logout
                    val intent = Intent(this, PhoneNumberVerification::class.java)
                    startActivity(intent)
                    FirebaseAuth.getInstance().signOut()
                    finish()

                    // clear sharedNumber in shared preferences
                    val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedAppLoginNumber,
                        Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                }
            }
            true
        }
        scrim.setOnClickListener { (bottomSheetBehavior as BottomSheetBehavior<*>).setState(BottomSheetBehavior.STATE_HIDDEN) }
        (bottomSheetBehavior as BottomSheetBehavior<*>).addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                scrim.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val baseColor = Color.BLACK
                val baseAlpha = 0.3f
                val offset = (slideOffset - -1f) / (1f - -1f) * (1f - 0f) + 0f
                val alpha = MathUtils.lerp(0f, 255f, offset * baseAlpha).roundToInt()
                val color = Color.argb(
                    alpha,
                    Color.red(baseColor),
                    Color.red(baseColor),
                    Color.red(baseColor)
                )
                scrim.setBackgroundColor(color)
            }
        })
    }

    private fun showAlert() {
        // setup the alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Terms and Conditions")
        val terms = R.string.terms
        builder.setMessage(terms)
        // add a button
        builder.setPositiveButton("OK", null)
        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // converts bitmap image to normal image
    @RequiresApi(Build.VERSION_CODES.O)
    private fun base64ToBitmap(b64: String): Bitmap? {
        val imageAsBytes: ByteArray = Base64.getDecoder().decode(b64)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

    private fun dashboardTabs() {
        dashboard_view_pager.adapter = DashboardViewPagerAdapter(supportFragmentManager, lifecycle)

        // Makes title and position to every tab as below
        TabLayoutMediator(dashboard_tab_layout, dashboard_view_pager){ tab,position ->
            when (position) {
                0 -> {
                    tab.text = "CHAT"
                }
                1 -> {
                    tab.text = "CIRCLES"
                }
                2 -> {
                    tab.text = "CONNECT"
                }
                3 -> {
                    tab.text = "CALLS"
                }
            }
        }.attach()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}