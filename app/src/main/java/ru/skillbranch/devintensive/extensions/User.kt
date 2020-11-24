package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.data.User
import ru.skillbranch.devintensive.utils.Utils

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

fun User.toUserView(): UserView {
    val nickName = Utils.transliteration("$firstName $lastName")

    val initials = Utils.toInitials(firstName, lastName)
    val status =
        if (lastVisit == null) "Еще ни разу не был" else if (isOnline) "online" else "Последний раз был ${lastVisit!!.humanizeDiff()}"

    return UserView(
        id,
        fullName = "$firstName $lastName",
        nickName = nickName,
        initials = initials,
        avatar = avatar,
        status = status
    )
}
