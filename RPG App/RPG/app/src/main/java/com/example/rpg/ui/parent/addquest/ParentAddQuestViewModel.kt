package com.example.rpg.ui.parent.addquest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Quest
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.QuestRepository
import com.example.rpg.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ParentAddQuestViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questRepository: QuestRepository,
    private val userRepository: UserRepository

) : ViewModel() {
    private val _children = MutableStateFlow<List<User>>(emptyList())
    val children = _children.asStateFlow()

    private val _selectedChild = MutableStateFlow<User?>(null)
    val selectedChild = _selectedChild.asStateFlow()

    private val _quest = MutableStateFlow(Quest())
    val quest = _quest.asStateFlow()

    private val _dueDate = MutableStateFlow<Date?>(null)
    val dueDate = _dueDate.asStateFlow()

    fun setDueDate(date: Date) {
        _dueDate.value = date
    }

    val parentId = FirebaseAuth.getInstance().currentUser?.uid
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchChildren() {
        if (parentId != null) {
            viewModelScope.launch {
                try {
                    val childrenList = userRepository.getChildren(parentId) // suspend function
                    _children.value = childrenList
                    if (_selectedChild.value == null && childrenList.isNotEmpty()) {
                        //_selectedChild.value = childrenList.first()
                        setChild(childrenList.first())
                    }
                } catch (e: Exception) {
                    _error.value = e.message
                }
            }
        }
    }

    fun setQuestTitle(newTitle: String) {
        _quest.value = _quest.value.copy(title = newTitle)
    }

    fun setQuestDescription(newDescription: String) {
        _quest.value = _quest.value.copy(description = newDescription)
    }

    fun setQuestRewardAmount(newAmount: Int) {
        try {
            val amount = newAmount.toInt()
            _quest.value = _quest.value.copy(rewardAmount = amount)
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun setDeadlineDate(newDate: Date) {
        // This is to avoid time resetting to current time if editing selected date
        // Grab stored date/time, if null use current date/time
        val previousDateTime = _dueDate.value ?: Date()
        // Merge old time with new date, if null old time is set to current time
        val tempCalendar = Calendar.getInstance().apply {
            time = newDate
            // Copy stored time h:m
            val previousTime = Calendar.getInstance().apply { time = previousDateTime }
            set(Calendar.HOUR_OF_DAY, previousTime.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, previousTime.get(Calendar.MINUTE))
        }
        val newDateTime = tempCalendar.time
        _dueDate.value = newDateTime
        _quest.value = _quest.value.copy(deadlineDate = newDateTime)
    }

    fun setDeadlineTime(hour: Int, minute: Int) {
        // Grab stored date/time, if null use current date/time
        val date = _dueDate.value ?: Date()
        // Update just the time
        val tempCalendar = Calendar.getInstance().apply {
            // Copy old date/time
            time = date
            // Set the selected time h:m
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        // Store copied date with new time
        val newDateTime = tempCalendar.time
        _dueDate.value = newDateTime
        _quest.value = _quest.value.copy(deadlineDate = newDateTime)
    }

    fun setChild(newChild: User) {
        _selectedChild.value = newChild
        _quest.value = _quest.value.copy(assignedTo = newChild.id)
        _quest.value = _quest.value.copy(assignee = parentId.toString())
    }

    fun addQuest() {
        val current = _quest.value


        if (current.title != "" && current.description != "" && current.deadlineDate != null) {
            viewModelScope.launch {
                questRepository.create(current)
            }
        }
    }
}