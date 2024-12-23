package orlo.ricknmorty.ricknmorty.presentation.viewmodels

import orlo.ricknmorty.ricknmorty.presentation.screens.Character.CharacterDetailsViewState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import orlo.ricknmorty.ricknmorty.data.repository.CharacterRepository
import orlo.ricknmorty.ricknmorty.presentation.components.character.DataPoint
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<CharacterDetailsViewState>(
        value = CharacterDetailsViewState.Loading
    )
    val stateFlow = _stateFlow.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        _stateFlow.update { return@update CharacterDetailsViewState.Loading }
        characterRepository.fetchCharacter(characterId).onSuccess { character ->
            val dataPoints = buildList {
                add(DataPoint("Last known location", character.location.name))
                add(DataPoint("Species", character.species))
                add(DataPoint("Gender", character.gender.displayName))
                character.type.takeIf { it.isNotEmpty() }?.let { type ->
                    add(DataPoint("Type", type))
                }
                add(DataPoint("Origin", character.origin.name))
            }
            _stateFlow.update {
                return@update CharacterDetailsViewState.Success(
                    character = character,
                    characterDataPoints = dataPoints
                )
            }

        }.onFailure { exception ->
            _stateFlow.update {
                return@update CharacterDetailsViewState.Error(
                    message = exception.message ?: "Unknown error occured"
                )
            }
        }
    }
}
