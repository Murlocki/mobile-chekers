package com.example.mobilecheckers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme
import kotlin.math.min

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }

    }
    @Composable
    fun MainScreen(modifier: Modifier = Modifier){
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.main_screen_layout,null)

                val startGameButton = view.findViewById<Button>(R.id.startButton)
                startGameButton.setOnClickListener({
                    val intent: Intent = Intent(this, GameActivity::class.java)
                    startActivity(intent)
                })
                val profileButton = view.findViewById<Button>(R.id.profileButton)
                profileButton.setOnClickListener({
                    val intent: Intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                })

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
}