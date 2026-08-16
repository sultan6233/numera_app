package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.NumerologyNumbers

object LuckyNumbers {
    fun luckyNumbers(numbers: NumerologyNumbers, personalYear: Int): List<Int> =
        listOf(numbers.lifePath, numbers.expression, personalYear).distinct()

    private val colorNames = mapOf(
        1 to "Красный", 2 to "Оранжевый", 3 to "Жёлтый", 4 to "Зелёный", 5 to "Голубой",
        6 to "Индиго", 7 to "Фиолетовый", 8 to "Коричневый", 9 to "Золотой",
        11 to "Серебряный", 22 to "Изумрудный", 33 to "Белый"
    )

    fun luckyColor(lifePath: Int): String = colorNames[lifePath] ?: colorNames.getValue(9)
}
