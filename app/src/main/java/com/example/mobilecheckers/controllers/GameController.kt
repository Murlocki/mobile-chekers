package com.example.mobilecheckers.controllers

import CheckerView
import GameViewModel
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecheckers.GameActivity
import com.example.mobilecheckers.MainActivity
import com.example.mobilecheckers.R
import com.example.mobilecheckers.models.Checker
import kotlin.math.min

class GameController(private val gameActivity: GameActivity) {
    private var viewModel:GameViewModel = ViewModelProvider(gameActivity).get(GameViewModel::class.java)
    private var highlightedCells = mutableListOf<View>()
    @RequiresApi(Build.VERSION_CODES.O)
    fun setupCheckersBoard(gridLayout: GridLayout) {
        val size = 8
        gridLayout.removeAllViews()

        gridLayout.post {
            val parentSize = min(gridLayout.width, gridLayout.height)
            val cellSize = parentSize / size

            for (row in 0 until size) {
                for (col in 0 until size) {
                    val isDarkCell = (row + col) % 2 == 1

                    val cell = FrameLayout(gameActivity).apply {
                        layoutParams = GridLayout.LayoutParams().apply {
                            width = cellSize
                            height = cellSize
                            columnSpec = GridLayout.spec(col)
                            rowSpec = GridLayout.spec(row)
                        }
                        setBackgroundColor(
                            if (isDarkCell) Color.parseColor("#7C4A33") else Color.parseColor(
                                "#DDB88C"
                            )
                        )
                    }

                    // Проверка, есть ли шашка на текущей клетке
                    val checker = viewModel.checkers.value?.find { it.row == row && it.col == col }

                    if (isDarkCell && checker != null) {
                        val checkerView =
                            CheckerView(gameActivity, checker) // Используем компонент для шашки
                        checkerView.setButtonListener(View.OnClickListener {
                            viewModel.selectChecker(checker)
                        })
                        cell.addView(checkerView)
                    }

                    gridLayout.addView(cell)
                }
            }
        }
        viewModel.selectedChecker.observe(gameActivity) {
            updateHighlights(gridLayout)
        }
    }
    fun setupNavPanel(navLayout: LinearLayout){
        val backButton: Button = navLayout.findViewById(R.id.backButton)
        backButton.setOnClickListener({
            val intent: Intent = Intent(gameActivity, MainActivity::class.java)
            viewModel.resetCheckers()
            gameActivity.startActivity(intent)
        })
    }
    private fun updateHighlights(gridLayout: GridLayout) {
        clearHighlights()
        val (normalMoves, attackMoves) = viewModel.getPossibleMovesWithHighlights()

        for ((row, col) in normalMoves) {
            val index = row * 8 + col
            val cell = gridLayout.getChildAt(index)
            cell?.setBackgroundColor(Color.GREEN)
            highlightedCells.add(cell)
        }

        for ((row, col) in attackMoves) {
            val index = row * 8 + col
            val cell = gridLayout.getChildAt(index)
            cell?.setBackgroundColor(Color.RED)
            highlightedCells.add(cell)
        }
    }

    private fun clearHighlights() {
        for (cell in highlightedCells) {
            cell.setBackgroundColor(Color.parseColor("#7C4A33"))
        }
        highlightedCells.clear()
    }

    fun resetCheckerState(bundle: Bundle?){
        if (bundle != null) {
            val savedCheckers = bundle.getParcelableArrayList<Checker>("checkers_state")
            savedCheckers!!.let {
                viewModel.restoreCheckersState(it)
            }
        }
    }
    fun saveCheckersState(outState: Bundle){
        val checkersState = viewModel.saveCheckersState()
        outState.putParcelableArrayList("checkers_state", ArrayList(checkersState))
    }
}