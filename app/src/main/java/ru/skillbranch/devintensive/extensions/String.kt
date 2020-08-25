package ru.skillbranch.devintensive.extensions

fun String.truncate(number: Int = 16): String {
    val strok: String = this.trim()
    var str = ""
    if (strok.length > number) {
        for (i in 0 until number) {
            if (!((i == (number - 1)) and (strok[i] == ' ')))
                str += strok[i]
        }
        str = str.trim() + "..."
    } else {
        str = strok
    }
    return str

}

fun String.stripHtml(): String {
    var str = ""
    for (i in this.split("""<[^>]*>|(\s+)""".toRegex())) {
        str += i.trim()
        str += " "
    }
    return str.trim().replace("""[&<>'"]""".toRegex(), "")
}