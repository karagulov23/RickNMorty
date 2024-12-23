package orlo.ricknmorty.ricknmorty.presentation.screens.Episode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import orlo.ricknmorty.network.models.domain.Episode
import orlo.ricknmorty.ricknmorty.presentation.components.episode.EpisodeRowComponent
import orlo.ricknmorty.ricknmorty.presentation.viewmodels.AllEpisodeViewModel
import orlo.ricknmorty.ricknmorty.utils.ErrorState
import orlo.ricknmorty.ricknmorty.utils.LoadingState

sealed interface AllEpisodeScreenViewState {
    object Error: AllEpisodeScreenViewState
    object Loading: AllEpisodeScreenViewState
    data class Success(val data: Map<String, List<Episode>>): AllEpisodeScreenViewState
}


@Composable
fun AllEpisodeScreen(
    viewModel: AllEpisodeViewModel = hiltViewModel(),
) {

    val episodeScreenState by viewModel.stateFlow.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.refreshAllEpisodes()
    }

    when(val state = episodeScreenState) {
        AllEpisodeScreenViewState.Error -> ErrorState()
        AllEpisodeScreenViewState.Loading -> LoadingState()
        is AllEpisodeScreenViewState.Success -> {
            Column {
                LazyColumn(
                    contentPadding = PaddingValues(all = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    state.data.forEach { mapEntry ->
                        item(key = mapEntry.key) {
                            Column {
                                Text(
                                    text = mapEntry.key,
                                    fontSize = 32.sp
                                )
                            }
                        }
                        mapEntry.value.forEach { episode->
                            item(key = episode.id) {
                                EpisodeRowComponent(
                                    episode = episode,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}