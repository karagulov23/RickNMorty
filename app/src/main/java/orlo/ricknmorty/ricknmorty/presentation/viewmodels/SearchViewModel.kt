package orlo.ricknmorty.ricknmorty.presentation.viewmodels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import orlo.ricknmorty.network.models.domain.CharacterStatus
import orlo.ricknmorty.ricknmorty.data.repository.CharacterRepository
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    @OptIn(ExperimentalFoundationApi::class)
    val searchTextFieldState = TextFieldState()

    sealed interface SearchState {
        object Empty : SearchState
        data class UserQuery(val query: String) : SearchState
    }

    sealed interface ScreenState {
        object Empty : ScreenState
        object Searching : ScreenState
        data class Error(val message: String) : ScreenState
        data class Content(
            val userQuery: String,
            val results: List<Character>,
            val filterState: FilterState
        ) : ScreenState {
            data class FilterState(
                val statuses: List<CharacterStatus>,
                val selectedStatuses: List<CharacterStatus>
            )
        }
    }

    private val _uiState = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalFoundationApi::class)
    private val searchTextState: StateFlow<SearchState> = snapshotFlow { searchTextFieldState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) SearchState.Empty else SearchState.UserQuery(it.toString()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
            initialValue = SearchState.Empty
        )



    fun toggleStatus(status: CharacterStatus) {
        _uiState.update {
            val currentState = (it as? ScreenState.Content) ?: return@update it
            val currentSelectedStatuses = currentState.filterState.selectedStatuses
            val newStatuses = if (currentSelectedStatuses.contains(status)) {
                currentSelectedStatuses - status
            } else {
                currentSelectedStatuses + status
            }
            return@update currentState.copy(
                filterState = currentState.filterState.copy(selectedStatuses = newStatuses)
            )
        }
    }

}