package orlo.ricknmorty.ricknmorty.presentation.screens

import CharacterDetailsViewState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import orlo.ricknmorty.network.ApiOperation
import orlo.ricknmorty.network.KtorClient
import orlo.ricknmorty.network.models.domain.Character
import orlo.ricknmorty.ricknmorty.presentation.components.character.DataPoint
import javax.inject.Inject


class CharacterRepository @Inject constructor(
    private val ktorClient: KtorClient
) {
    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character> {
        return ktorClient.getCharacter(characterId)
    }
}


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
