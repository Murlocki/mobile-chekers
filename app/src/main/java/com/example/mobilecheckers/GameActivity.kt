package com.example.mobilecheckers
import CheckerView
import GameViewModel
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
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
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
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecheckers.models.Checker
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme
import kotlin.math.min

class GameActivity : ComponentActivity() {
    private lateinit var viewModel: GameViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(Modifier.padding(innerPadding))
                }
            }
        }
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        if (savedInstanceState != null) {
            val savedCheckers = savedInstanceState.getParcelableArrayList<Checker>("checkers_state")
            savedCheckers!!.let {
                viewModel.restoreCheckersState(it)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun GameScreen(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.game_layout,null)
                val gridLayout: GridLayout = view.findViewById(R.id.checkersBoard);
                setupCheckersBoard(gridLayout)

                val navLayout: LinearLayout = view.findViewById(R.id.navPanel)
                setupNavPanel(navLayout)

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val checkersState = viewModel.saveCheckersState()
        outState.putParcelableArrayList("checkers_state", ArrayList(checkersState))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCheckersBoard(gridLayout: GridLayout) {
        val size = 8
        gridLayout.removeAllViews()

        gridLayout.post {
            val parentSize = min(gridLayout.width, gridLayout.height)
            val cellSize = parentSize / size

            for (row in 0 until size) {
                for (col in 0 until size) {
                    val isDarkCell = (row + col) % 2 == 1

                    val cell = FrameLayout(this).apply {
                        layoutParams = GridLayout.LayoutParams().apply {
                            width = cellSize
                            height = cellSize
                            columnSpec = GridLayout.spec(col)
                            rowSpec = GridLayout.spec(row)
                        }
                        setBackgroundColor(if (isDarkCell) Color.parseColor("#7C4A33") else Color.parseColor("#DDB88C"))
                    }

                    // Проверка, есть ли шашка на текущей клетке
                    val checker = viewModel.checkers.value?.find { it.row == row && it.col == col }

                    if (isDarkCell && checker != null) {
                        val checkerView = CheckerView(this, checker) // Используем компонент для шашки
                        checkerView.setButtonListener(View.OnClickListener {
                            viewModel.calculatePossibleMoves(checker,viewModel.checkers.value!!,8)
                        })
                        cell.addView(checkerView)
                    }

                    gridLayout.addView(cell)
                }
            }
        }
    }
    private fun setupNavPanel(navLayout: LinearLayout){
        val backButton: Button = navLayout.findViewById(R.id.backButton)
        backButton.setOnClickListener({
            val intent: Intent = Intent(this, MainActivity::class.java)
            viewModel.resetCheckers()
            startActivity(intent)
        })
    }

}