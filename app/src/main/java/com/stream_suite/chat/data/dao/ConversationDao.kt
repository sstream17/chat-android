package com.stream_suite.chat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stream_suite.chat.data.entity.Conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM Conversation ORDER BY name DESC")
    fun getAllConversations(): Flow<List<Conversation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(conversation: Conversation)

    @Query("DELETE FROM Conversation")
    suspend fun deleteAll()
}