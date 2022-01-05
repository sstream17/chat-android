package com.stream_suite.chat.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Conversation(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}