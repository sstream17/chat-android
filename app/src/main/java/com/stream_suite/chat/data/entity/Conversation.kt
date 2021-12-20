package com.stream_suite.chat.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)