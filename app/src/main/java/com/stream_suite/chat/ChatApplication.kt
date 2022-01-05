package com.stream_suite.chat

import android.app.Application
import com.stream_suite.chat.data.ChatDatabase
import com.stream_suite.chat.data.repository.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ChatApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ChatDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ConversationRepository(database.conversationDao()) }
}