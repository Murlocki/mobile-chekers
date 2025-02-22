import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilecheckers.models.Checker
import kotlin.random.Random

class GameViewModel : ViewModel() {
    val checkers = MutableLiveData<MutableList<Checker>>()
    var isPlayerWhite: Boolean = Random.nextBoolean()
    var currentChecker: Checker? = null
    init {
        if (checkers.value.isNullOrEmpty()) {
            loadCheckers() // Загрузим шашки, если они ещё не загружены
        }
    }

    private fun loadCheckers() {
        val loadedCheckers = mutableListOf<Checker>()

        // Шашки врага
        for (row in 0..2) {
            for (col in 0 until 8 step 2) {
                loadedCheckers.add(Checker(row, if (row % 2 == 0) col + 1 else col, isPlayerWhite))
            }
        }

        // Шашки игрока
        for (row in 5..7) {
            for (col in 0 until 8 step 2) {
                loadedCheckers.add(Checker(row, if (row % 2 == 0) col + 1 else col, !isPlayerWhite))
            }
        }

        checkers.value = loadedCheckers
    }

    fun saveCheckersState(): List<Checker> {
        return checkers.value ?: emptyList()
    }

    fun restoreCheckersState(savedCheckers: List<Checker>) {
        checkers.value = savedCheckers.toMutableList()
    }
    fun resetCheckers(){
        checkers.value?.clear()
    }
    fun calculatePossibleMoves(checker: Checker,checkers: MutableList<Checker>, boardSize: Int = 8): List<Pair<Int, Int>> {
        val possibleMoves = mutableListOf<Pair<Int, Int>>()

        // Направления для движения (вверх для белых, вниз для черных)
        val directions = if (checker.isWhite) {
            listOf(Pair(-1, -1), Pair(-1, 1)) // Белые двигаются вверх по диагонали
        } else {
            listOf(Pair(1, -1), Pair(1, 1)) // Черные двигаются вниз по диагонали
        }

        // Для каждого направления проверяем клетки
        for ((rowOffset, colOffset) in directions) {
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset

            // Проверяем, чтобы не выйти за пределы доски
            if (newRow in 0 until boardSize && newCol in 0 until boardSize) {
                // Проверяем, если клетка свободна
                if (!isOccupied(newRow, newCol, checkers)) {
                    possibleMoves.add(Pair(newRow, newCol))
                }
            }
        }

        // Теперь проверим прыжки через шашку противника (по диагонали)
        val jumpDirections = if (checker.isWhite) {
            listOf(Pair(-2, -2), Pair(-2, 2)) // Белые шашки могут прыгать вверх
        } else {
            listOf(Pair(2, -2), Pair(2, 2)) // Черные шашки могут прыгать вниз
        }

        for ((rowOffset, colOffset) in jumpDirections) {
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset

            // Проверяем, чтобы не выйти за пределы доски
            if (newRow in 0 until boardSize && newCol in 0 until boardSize) {
                // Проверяем, есть ли шашка противника по диагонали
                val middleRow = checker.row + rowOffset / 2
                val middleCol = checker.col + colOffset / 2
                val middleChecker = getCheckerAt(middleRow, middleCol, checkers)

                // Если клетка свободна и через неё можно "прыгнуть" через шашку противника
                if (middleChecker != null && middleChecker.isWhite != checker.isWhite && !isOccupied(newRow, newCol, checkers)) {
                    possibleMoves.add(Pair(newRow, newCol))
                }
            }
        }
        println(possibleMoves)
        return possibleMoves
    }

    // Проверяем, есть ли шашка в указанной клетке
    fun isOccupied(row: Int, col: Int, checkers: List<Checker>): Boolean {
        return checkers.any { it.row == row && it.col == col }
    }

    // Получаем шашку в указанной клетке
    fun getCheckerAt(row: Int, col: Int, checkers: List<Checker>): Checker? {
        return checkers.find { it.row == row && it.col == col }
    }
}
