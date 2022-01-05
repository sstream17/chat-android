package com.stream_suite.chat.data.repository

import androidx.annotation.WorkerThread
import com.stream_suite.chat.data.dao.ConversationDao
import com.stream_suite.chat.data.entity.Conversation
import kotlinx.coroutines.flow.Flow

class ConversationRepository(private val conversationDao: ConversationDao) {
    val allConversations: Flow<List<Conversation>> = conversationDao.getAllConversations()

    @WorkerThread
    suspend fun insert(conversation: Conversation) {
        conversationDao.insert(conversation)
    }
}