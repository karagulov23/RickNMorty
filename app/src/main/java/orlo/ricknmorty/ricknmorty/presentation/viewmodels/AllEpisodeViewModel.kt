package orlo.ricknmorty.ricknmorty.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import orlo.ricknmorty.ricknmorty.data.repository.EpisodesRepository
import orlo.ricknmorty.ricknmorty.presentation.screens.Episode.AllEpisodeScreenViewState
import javax.inject.Inject

@HiltViewModel
class AllEpisodeViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository
) : ViewModel() {
    private val _stateFlow =
        MutableStateFlow<AllEpisodeScreenViewState>(AllEpisodeScreenViewState.Loading)
    val stateFlow = _stateFlow.asStateFlow()



    fun fetchEpisode(episodeId: Int) = viewModelScope.launch {
        episodesRepository.fetchEpisode(episodeId)
            .onSuccess { episode ->
            }
    }


    fun refreshAllEpisodes(forceRefresh: Boolean = true) = viewModelScope.launch {
        if (forceRefresh) _stateFlow.update {
            AllEpisodeScreenViewState.Loading
        }
        episodesRepository.fetchAllEpisodes()
            .onSuccess { episodeList ->
                _stateFlow.update {
                    AllEpisodeScreenViewState.Success(
                        data = episodeList.groupBy {
                            it.seasonNumber.toString()
                        }.mapKeys {
                            "Season ${it.key}"
                        }
                    )
                }
            }
            .onFailure {
                _stateFlow.update {
                    AllEpisodeScreenViewState.Error
                }
            }
    }

}