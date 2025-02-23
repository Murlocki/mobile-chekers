package com.example.mobilecheckers
import PlayerAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
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
import com.example.mobilecheckers.models.Player
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme


class RatingActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RatingScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun RatingScreen(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.rating_layout,null)

                val players: MutableList<Player> = ArrayList<Player>()

                // Добавьте данные игроков в список
                players.add(Player("Player1", 1,10, 5, 15.5F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 2,8, 7, 20.0F))
                players.add(Player("Player2", 100,8, 7, 20.0F))



                val listView:ListView = view.findViewById<ListView>(R.id.lvPlayers)
                val adapter: PlayerAdapter = PlayerAdapter(context, java.util.ArrayList(players))
                listView.adapter = adapter

                val navLayout: LinearLayout = view.findViewById(R.id.navPanel)
                setupNavPanel(navLayout)

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