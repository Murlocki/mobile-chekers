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
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme
import kotlin.math.min

class GameActivity : ComponentActivity() {
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
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun GameScreen(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.game_layout,null)
                val gridLayout: GridLayout = view.findViewById(R.id.checkersBoard);
                setupCheckersBoard(this, gridLayout)

                val statLayout: LinearLayout = view.findViewById(R.id.playerStat)
                setupTextValue(statLayout)

                val navLayout: LinearLayout = view.findViewById(R.id.navPanel)
                setupNavPanel(navLayout)

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCheckersBoard(context: Context, gridLayout: GridLayout) {
        val size = 8 // Размер поля 8x8
        gridLayout.removeAllViews() // Очищаем перед заполнением

        gridLayout.post {
            val parentSize = min(gridLayout.width, gridLayout.height)
            val cellSize = parentSize / size
            val buttonSize = (cellSize * 0.8).toInt() // Шашки занимают 90% ячейки

            for (row in 0 until size) {
                for (col in 0 until size) {
                    val isDarkCell = (row + col) % 2 == 1 // Только темные клетки

                    // Создаем ячейку
                    val cell = FrameLayout(context).apply {
                        layoutParams = GridLayout.LayoutParams().apply {
                            width = cellSize
                            height = cellSize
                            columnSpec = GridLayout.spec(col)
                            rowSpec = GridLayout.spec(row)
                        }
                        setBackgroundColor(if (isDarkCell) Color.parseColor("#7C4A33") else Color.parseColor("#DDB88C"))
                    }

                    // Если клетка темная и в ней должна быть шашка
                    if (isDarkCell && (row < 3 || row > 4)) {
                        val isWhiteChecker = row > 4 // Белые шашки внизу

                        val checker = Button(context).apply {
                            val buttonLayoutParams = FrameLayout.LayoutParams(buttonSize, buttonSize) // Используем FrameLayout.LayoutParams
                            buttonLayoutParams.gravity = Gravity.CENTER // Центрируем кнопку внутри ячейки
                            layoutParams = buttonLayoutParams // Устанавливаем параметры для кнопки

                            background = ShapeDrawable(OvalShape()).apply {
                                paint.color = if (isWhiteChecker) Color.WHITE else Color.BLACK
                            }

                            // Устанавливаем текст в зависимости от цвета шашки
                            text = if (isWhiteChecker) "⚪" else "⚫"
                            setTextColor(if (isWhiteChecker) Color.BLACK else Color.WHITE) // Устанавливаем контрастный цвет текста
                            textAlignment = View.TEXT_ALIGNMENT_CENTER // Центрируем текст внутри кнопки
                            setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM)
                        }

                        cell.addView(checker) // Добавляем шашку в ячейку
                    }

                    gridLayout.addView(cell) // Добавляем ячейку в поле
                }
            }
        }
    }

    private fun setupTextValue(statLayout: LinearLayout){
        val playerMove:LinearLayout = statLayout.findViewById(R.id.playerMoveText)
        val playerMoveText:TextView = playerMove.findViewById(R.id.textField)
        playerMoveText.text = "Ход игрока"

        val playerMoveCount:LinearLayout = statLayout.findViewById(R.id.playerMoveCountText)
        val playerMoveCountText:TextView = playerMoveCount.findViewById(R.id.textField)
        playerMoveCountText.text = "Количество ходов"


        val whiteCount:LinearLayout = statLayout.findViewById(R.id.whiteCountText)
        val whiteCountText:TextView = whiteCount.findViewById(R.id.textField)
        whiteCountText.text = "Белых осталось"


        val blackCount:LinearLayout = statLayout.findViewById(R.id.blackCountText)
        val blackCountText:TextView = blackCount.findViewById(R.id.textField)
        blackCountText.text = "Черных осталосьа"

    }

    private fun setupNavPanel(navLayout: LinearLayout){
        val backButton: Button = navLayout.findViewById(R.id.backButton)
        backButton.setOnClickListener({
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
    }
}