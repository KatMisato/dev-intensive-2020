package ru.skillbranch.devintensive.models

class UserView(
    val id: String,
    var fullName: String,
    var nickName: String,
    var avatar: String? = null,
    var status: String = "offline",
    var initials: String?
) {

    fun printMe() {
        println(
            """
            id: $id,
            fullName: $fullName,
            nickName: $nickName,
            avatar: $avatar,
            status: $status,
            initials: $initials
        """.trimIndent()
        )
    }
}