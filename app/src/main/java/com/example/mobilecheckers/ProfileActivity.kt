package com.example.mobilecheckers
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme


class ProfileActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ProfileScreen(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.profile_layout,null)

                val navLayout: LinearLayout = view.findViewById(R.id.navPanel)
                setupNavPanel(navLayout)

                val intent = intent
                println(intent.getStringExtra("nickname"))

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
    private fun setupNavPanel(navLayout: LinearLayout){
        val backButton: Button = navLayout.findViewById(R.id.backButton)
        backButton.setOnClickListener({
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
    }
}