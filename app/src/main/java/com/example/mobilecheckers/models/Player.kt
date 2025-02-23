package com.example.mobilecheckers.models


data class Player(
    val nickname: String,
    val wins: Int,
    val losses: Int,
    val averageMoves: Double,
    val id: Long = 0
) {
    // Рейтинг рассчитывается как сумма побед, поражений и среднего числа ходов
    val rating: Double
        get() = wins - losses + averageMoves
}


