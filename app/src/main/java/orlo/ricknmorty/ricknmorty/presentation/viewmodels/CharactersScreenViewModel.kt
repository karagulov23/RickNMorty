package orlo.ricknmorty.ricknmorty.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import orlo.ricknmorty.network.models.domain.CharacterPage
import orlo.ricknmorty.ricknmorty.data.repository.CharacterRepository
import orlo.ricknmorty.ricknmorty.presentation.screens.Character.CharacterScreenViewState
import javax.inject.Inject

@HiltViewModel
class CharactersScreenViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {

    private val _viewState = MutableStateFlow<CharacterScreenViewState>(CharacterScreenViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<CharacterPage>()

    fun fetchInitialPage() = viewModelScope.launch {
        if(fetchedCharacterPages.isNotEmpty()) return@launch
        val initialPage = characterRepository.fetchCharacterPage(1)
        initialPage.onSuccess { characterPage ->
            fetchedCharacterPages.clear()
            fetchedCharacterPages.add(characterPage)

            _viewState.update { return@update CharacterScreenViewState.GridDisplay(characters = characterPage.characters) }
        }.onFailure {

        }
    }

    fun fetchNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchedCharacterPages.size + 1
        characterRepository.fetchCharacterPage(page = nextPageIndex).onSuccess { characterPage ->
            fetchedCharacterPages.add(characterPage)
            _viewState.update { currentState ->
                val currentCharacters = (currentState as? CharacterScreenViewState.GridDisplay)?.characters ?: emptyList()
                return@update CharacterScreenViewState.GridDisplay(characters = currentCharacters + characterPage.characters)
            }
        }.onFailure {
            // todo
        }
    }
}