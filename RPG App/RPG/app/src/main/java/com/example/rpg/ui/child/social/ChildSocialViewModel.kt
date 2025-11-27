package com.example.rpg.ui.child.social

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpg.data.model.Stats
import com.example.rpg.data.model.User
import com.example.rpg.data.repository.AuthRepository
import com.example.rpg.data.repository.StatsRepository
import com.example.rpg.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildSocialViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {
    var isLoadingFriends by mutableStateOf(false)
        private set
    var errorMessageFriends by mutableStateOf<String?>(null)
        private set

    val currentUserIdFlow: Flow<String?> = authRepository.currentUserIdFlow

    private val friendsFlow: Flow<List<User>> =
        currentUserIdFlow.flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else userRepository.getChildrenFlow(uid)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val friendsLeaderboard: StateFlow<List<Pair<User, Stats>>> =
        currentUserIdFlow.flatMapLatest { currentUid ->
            if (currentUid == null) return@flatMapLatest flowOf(emptyList())

            // Combine current user + friends
            combine(
                userRepository.getUserByUidFlow(currentUid),
                friendsFlow
            ) { currentUser, friendsList ->
                val list = mutableListOf<User>()
                currentUser?.let { list.add(it) }
                list.addAll(friendsList)
                list
            }
                .flatMapLatest { users ->
                    if (users.isEmpty()) flowOf(emptyList())
                    else {
                        combine(
                            users.map { user ->
                                statsRepository
                                    .getStatsFlow(flowOf(user.id))
                                    .map { stats -> user to stats }
                            }
                        ) { arr -> arr.toList() }
                    }
                }

        }
            .map { list -> list.sortedByDescending { it.second.totalXP } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addFriendByUsername(
        username: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoadingFriends = true
            errorMessageFriends = null

            val currentUid = currentUserIdFlow.firstOrNull()
            if (currentUid == null) {
                errorMessageFriends = "User not logged in."
                isLoadingFriends = false
                return@launch
            }

            try {
                val userToAdd = userRepository.getUserByUsername(username)
                if (userToAdd == null) {
                    errorMessageFriends = "User not found."
                    return@launch
                }

                val existingFriends = friendsFlow.first().map { it.id }
                if (userToAdd.id in existingFriends) {
                    errorMessageFriends = "This user is already your friend."
                    return@launch
                }

                userRepository.addChild(currentUid, userToAdd.id)

                onSuccess()

            } catch (e: Exception) {
                errorMessageFriends = e.message ?: "Something went wrong."
            } finally {
                isLoadingFriends = false
            }
        }
    }
}