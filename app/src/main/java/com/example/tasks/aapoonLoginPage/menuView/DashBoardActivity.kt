package com.example.tasks.aapoonLoginPage.menuView

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.math.MathUtils
import com.google.android.material.navigation.NavigationView
import kotlin.math.roundToInt

class DashBoardActivity : AppCompatActivity() {

    private var bottomSheetBehavior: BottomSheetBehavior<NavigationView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        initComponent()
    }

    private fun initComponent() {
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
            Toast.makeText(
                applicationContext,
                menuItem.title.toString() + " Selected",
                Toast.LENGTH_SHORT
            ).show()
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

}