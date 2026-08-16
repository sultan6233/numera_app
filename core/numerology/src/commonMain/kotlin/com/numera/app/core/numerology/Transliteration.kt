package com.numera.app.core.numerology

/**
 * The Pythagorean letter-number table is defined for the Latin alphabet, but most of this app's
 * users type Cyrillic names. Standard practice (and what every Russian-market numerology
 * reference does) is to transliterate to Latin first, then run the normal table - not invent a
 * separate Cyrillic table with no historical basis. Each Cyrillic letter maps to its closest
 * Latin sound, including multi-letter ones (ж→zh, ч→ch) - every resulting Latin letter still
 * contributes its own value to the sum, same as it would in a Latin name.
 */
internal object Transliteration {
    private val map = mapOf(
        'а' to "a", 'б' to "b", 'в' to "v", 'г' to "g", 'д' to "d", 'е' to "e", 'ё' to "e",
        'ж' to "zh", 'з' to "z", 'и' to "i", 'й' to "y", 'к' to "k", 'л' to "l", 'м' to "m",
        'н' to "n", 'о' to "o", 'п' to "p", 'р' to "r", 'с' to "s", 'т' to "t", 'у' to "u",
        'ф' to "f", 'х' to "h", 'ц' to "c", 'ч' to "ch", 'ш' to "sh", 'щ' to "sch", 'ъ' to "",
        'ы' to "y", 'ь' to "", 'э' to "e", 'ю' to "yu", 'я' to "ya"
    )

    fun toLatin(text: String): String = buildString {
        for (ch in text) {
            val replacement = map[ch.lowercaseChar()]
            when {
                replacement == null -> append(ch)
                ch.isUpperCase() -> append(replacement.replaceFirstChar { it.uppercase() })
                else -> append(replacement)
            }
        }
    }
}
