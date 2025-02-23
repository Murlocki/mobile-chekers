package com.example.mobilecheckers.controllers

import CheckerView
import GameViewModel
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroupOverlay
import android.view.ViewOverlay
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecheckers.GameActivity
import com.example.mobilecheckers.MainActivity
import com.example.mobilecheckers.R
import com.example.mobilecheckers.models.Checker
import com.example.mobilecheckers.ui.theme.BlackCell
import com.example.mobilecheckers.ui.theme.WhiteCell
import kotlin.math.min

private const val s = "#7C4A33"

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
                            if (isDarkCell) BlackCell.toArgb() else WhiteCell.toArgb()
                        )
                    }

                    cell.setOnClickListener {
                        changeCheckerPosition(gridLayout,cell)
                    }

                    // Проверка, есть ли шашка на текущей клетке
                    val checker = viewModel.checkers.value?.find { it.row == row && it.col == col }

                    if (isDarkCell && checker != null) {
                        val checkerView =
                            CheckerView(gameActivity, checker) // Используем компонент для шашки
                        checkerView.setButtonListener {
                            viewModel.selectChecker(checker)
                        }
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
            cell.setBackgroundColor(BlackCell.toArgb())
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

    // Функция перемещения шашки на выбранную клетку
    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeCheckerPosition(gridLayout: GridLayout,cell:FrameLayout) {
        // Получаем координаты новой ячейки
        val position = gridLayout.indexOfChild(cell)
        val row = position / 8
        val col = position % 8
        // Если это клетка для перемещения
        if (viewModel.currentCheckerValue() != null && viewModel.currentMove.contains(Pair(row, col))) {
            // Получаем старые координаты шашки
            val oldRow:Int = viewModel.currentCheckerValue()!!.row
            val oldCol:Int = viewModel.currentCheckerValue()!!.col

            //Получаем старую ячейку и старый View шашки
            val oldCell = gridLayout.getChildAt(oldRow * 8 + oldCol) as FrameLayout
            val checkerView = oldCell.children.first() as CheckerView// Удаляем старое представление
            // Двигаем с анимацией
            moveCheckerWithAnimation(checkerView,row,col,oldRow,oldCol,gridLayout,cell)
        }
    }

    // Функция перемещения шашки с запуском анимации и реальным перемещением
    @RequiresApi(Build.VERSION_CODES.O)
    private fun moveCheckerWithAnimation(
        checkerView: CheckerView,
        newRow: Int,
        newCol: Int,
        oldRow: Int,
        oldCol: Int,
        gridLayout: GridLayout,
        newCell: FrameLayout
    ) {
        // Получаем размеры ячеек доски (предполагается, что доска квадратная)
        val cellSize = gridLayout.width / 8

        // Конечные координаты для перемещения
        val endX = (newCol - oldCol) * cellSize.toFloat()
        val endY = (newRow - oldRow) * cellSize.toFloat()
        // Снимаем шашку с текущей ячейки
        gridLayout.removeView(checkerView)

        // Получаем родительский ViewGroup (это overlay, который используется для анимации)
        val parentView = gridLayout.rootView as ViewGroup
        val overlay = parentView.overlay

        // Помещаем шашку в overlay для анимации
        overlay.add(checkerView)


        // Запускаем анимацию, чтобы двигаться к конечным координатам
        val endAnimationCallBack = fun(){ changeRealCheckerPosition(overlay,checkerView,newRow,newCol,newCell) }
        checkerView.animateMoveSequence(Pair(endX,endY),endAnimationCallBack)
    }

    // Функция изменения реальной позиции шашки
    @RequiresApi(Build.VERSION_CODES.O)
    fun changeRealCheckerPosition(overlay: ViewGroupOverlay, checkerView: CheckerView, newRow: Int, newCol: Int, newCell: FrameLayout){
        // После завершения анимации возвращаем шашку в GridLayout
        overlay.remove(checkerView)
        // Обновляем позицию шашки в модели
        viewModel.moveChecker(newRow, newCol)
        // Создаем новую кнопку потому что overlay пошел нахрен
        val newCheckerView = CheckerView(gameActivity, checkerView.checker)
        newCheckerView.setButtonListener(View.OnClickListener {
            viewModel.selectChecker(checkerView.checker)
        })
        newCell.addView(newCheckerView)
        viewModel.clearCurrentChecker()
    }



}