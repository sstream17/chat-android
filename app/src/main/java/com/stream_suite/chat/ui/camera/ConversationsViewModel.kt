package com.stream_suite.chat.ui.camera

import androidx.lifecycle.*
import com.stream_suite.chat.data.entity.Conversation
import com.stream_suite.chat.data.repository.ConversationRepository
import kotlinx.coroutines.launch

class ConversationsViewModel(private val repository: ConversationRepository) : ViewModel() {
    val allConversations: LiveData<List<Conversation>> = repository.allConversations.asLiveData()

    fun insert(conversation: Conversation) = viewModelScope.launch {
        repository.insert(conversation)
    }

    class ConversationViewModelFactory(private val repository: ConversationRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConversationsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ConversationsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}