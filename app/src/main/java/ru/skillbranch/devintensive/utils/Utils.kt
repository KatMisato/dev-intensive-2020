package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return if (firstName != "") Pair(firstName, lastName)
        else Pair(null, null)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val firstSym = firstName?.getOrNull(0)
        val lastSym = lastName?.getOrNull(0)

        val firstIsNull = firstSym == null || firstSym == ' '
        val lastIsNull = lastSym == null || lastSym == ' '

        if (firstIsNull && lastIsNull) {
            return null
        }

        if (firstIsNull) {
            return lastSym!!.toUpperCase().toString()
        }

        if (lastIsNull) {
            return firstSym!!.toUpperCase().toString()
        }

        return (firstSym.toString() + lastSym.toString()).toUpperCase()
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var result = ""
        payload.forEach {
            val char = when (it.toLowerCase()) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е' -> "e"
                'ё' -> "e"
                'ж' -> "zh"
                'з' -> "z"
                'и' -> "i"
                'й' -> "i"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "h"
                'ц' -> "c"
                'ч' -> "ch"
                'ш' -> "sh"
                'щ' -> "sh'"
                'ъ' -> ""
                'ы' -> "i"
                'ь' -> ""
                'э' -> "e"
                'ю' -> "yu"
                'я' -> "ya"
                ' ' -> divider
                else -> it.toString()
            }
            result += if (it.isUpperCase()) char.capitalize() else char
        }
        return result
    }
}

