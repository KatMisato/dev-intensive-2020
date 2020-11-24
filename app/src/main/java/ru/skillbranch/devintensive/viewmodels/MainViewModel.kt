package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.repositories.ChatRepository
import ru.skillbranch.devintensive.utils.DataGenerator

class MainViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository

    private var chats: LiveData<List<ChatItem>>

    init {
        chats = Transformations.map(chatRepository.loadChats()) { chats ->
            val chatsArchived = chats.filter { it.isArchived }
            if (chatsArchived.isEmpty()) {
                return@map chats.map { it.toChatItem() }.sortedBy { it.id.toInt() }
            } else {
                val listWithArchive = mutableListOf<ChatItem>()
                listWithArchive.add(0, createArchiveItem(chatsArchived))
                listWithArchive.addAll(chats.filter { !it.isArchived }.map { it.toChatItem() })
                listWithArchive.sortedBy { it.id.toInt() }
                return@map listWithArchive
            }
        }
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chats.value!!

            result.value = if (queryStr.isEmpty()) chats else chats.filter {
                it.title.contains(
                    queryStr,
                    true
                ) || it.chatType == ChatType.ARCHIVE
            }
        }
        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        if (text == null) query.value = ""
        else query.value = text
    }

    private fun createArchiveItem(chatsArchived: List<Chat>): ChatItem {
        val messageCount =
            chatsArchived.fold(0) { acc, chat -> acc + chat.unreadableMessageCount() }

        val lastChatItem =
            if (chatsArchived.none { it.unreadableMessageCount() != 0 }) chatsArchived.last() else
                chatsArchived.filter { it.unreadableMessageCount() != 0 }
                    .maxBy { it.lastMessageDate()!! }!!

        return ChatItem(
            "-1",
            "",
            "",
            "",
            lastChatItem.lastMessageShort().first,
            messageCount,
            lastChatItem.lastMessageDate()?.shortFormat(),
            false,
            ChatType.ARCHIVE,
            "@${lastChatItem.lastMessageShort().second}"
        )
    }
}