import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilecheckers.models.Checker
import kotlin.random.Random

class GameViewModel : ViewModel() {
    val checkers = MutableLiveData<MutableList<Checker>>()
    var isPlayerWhite: Boolean = Random.nextBoolean()
    var selectedChecker = MutableLiveData<Checker?>()
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
    fun selectChecker(checker: Checker?) {
        selectedChecker.value = if (selectedChecker.value == checker || checker?.isWhite == isPlayerWhite) null else checker
    }

    fun getPossibleMovesWithHighlights(): Pair<List<Pair<Int, Int>>, List<Pair<Int, Int>>> {
        val checker = selectedChecker.value ?: return Pair(emptyList(), emptyList())
        val possibleMoves = mutableListOf<Pair<Int, Int>>()
        val attackMoves = mutableListOf<Pair<Int, Int>>()

        val directions = listOf(Pair(-1, -1), Pair(-1, 1))


        for ((rowOffset, colOffset) in directions) {
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset
            if (newRow in 0 until 8 && newCol in 0 until 8 && !isOccupied(newRow, newCol)) {
                possibleMoves.add(Pair(newRow, newCol))
            }
        }

        val jumpDirections = listOf(Pair(-2, -2), Pair(-2, 2))


        for ((rowOffset, colOffset) in jumpDirections) {
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset
            val middleRow = checker.row + rowOffset / 2
            val middleCol = checker.col + colOffset / 2
            val middleChecker = getCheckerAt(middleRow, middleCol)

            if (newRow in 0 until 8 && newCol in 0 until 8 &&
                middleChecker != null && middleChecker.isWhite != checker.isWhite &&
                !isOccupied(newRow, newCol)) {
                attackMoves.add(Pair(newRow, newCol))
            }
        }

        return Pair(possibleMoves, attackMoves)
    }

    private fun isOccupied(row: Int, col: Int): Boolean {
        return checkers.value?.any { it.row == row && it.col == col } == true
    }

    private fun getCheckerAt(row: Int, col: Int): Checker? {
        return checkers.value?.find { it.row == row && it.col == col }
    }
}
