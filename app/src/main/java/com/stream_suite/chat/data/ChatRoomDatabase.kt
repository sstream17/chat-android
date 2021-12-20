package com.stream_suite.chat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.stream_suite.chat.data.dao.ConversationDao
import com.stream_suite.chat.data.entity.Conversation

@Database(entities = [Conversation::class], version = 1, exportSchema = false)
abstract class ChatRoomDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ChatRoomDatabase? = null

        fun getDatabase(context: Context): ChatRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatRoomDatabase::class.java,
                    "chat_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}