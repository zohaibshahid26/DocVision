package com.example.find_a_doctor

import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : ComponentActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        val menuButton: ImageButton = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val gridLayout: GridLayout = findViewById(R.id.gridLayout)
        val items = listOf(
            GridItem(R.drawable.ic_video_consultation, "Video Consultation"),
            GridItem(R.drawable.ic_clinic_visit, "In-Clinic Visit"),
            // Add more items
        )

        for (item in items) {
            val itemView = layoutInflater.inflate(R.layout.item_layout, gridLayout, false)
            val imageView: ImageView = itemView.findViewById(R.id.item_image)
            val textView: TextView = itemView.findViewById(R.id.item_text)

            imageView.setImageResource(item.image as Int)
            textView.text = item.s

            gridLayout.addView(itemView)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
