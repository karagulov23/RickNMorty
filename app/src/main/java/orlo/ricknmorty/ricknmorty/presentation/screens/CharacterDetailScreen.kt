import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.SubcomposeAsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import orlo.ricknmorty.network.ApiOperation
import orlo.ricknmorty.network.KtorClient
import orlo.ricknmorty.network.models.domain.Character
import orlo.ricknmorty.ricknmorty.presentation.components.character.CharacterDetailsNamePlateComponent
import orlo.ricknmorty.ricknmorty.presentation.components.character.DataPoint
import orlo.ricknmorty.ricknmorty.presentation.components.character.DataPointComponent
import orlo.ricknmorty.ricknmorty.presentation.screens.CharacterDetailViewModel
import orlo.ricknmorty.ricknmorty.presentation.ui.theme.RickAction


sealed interface CharacterDetailsViewState {
    object Loading : CharacterDetailsViewState
    data class Error(val message: String) : CharacterDetailsViewState
    data class Success(
        val character: Character,
        val characterDataPoints: List<DataPoint>
    ) : CharacterDetailsViewState
}


@Composable
fun CharacterDetailsScreen(
    //ktorClient: KtorClient,
    viewmodel: CharacterDetailViewModel = hiltViewModel(),
    characterId: Int,
    onEpisodeClicked: (Int) -> Unit
) {
    //var character by remember { mutableStateOf<Character?>(null) }

    LaunchedEffect(key1 = Unit, block = {
        viewmodel.fetchCharacter(characterId)
    })

    val state by viewmodel.stateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        when (val viewState = state) {
            CharacterDetailsViewState.Loading -> item { LoadingState() }
            is CharacterDetailsViewState.Error -> {}
            is CharacterDetailsViewState.Success -> {

                // Image
                item {
                    SubcomposeAsyncImage(
                        model = viewState.character.imageUrl,
                        contentDescription = "Character image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp)),
                        loading = { LoadingState() }
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Name plate
                item {
                    CharacterDetailsNamePlateComponent(
                        name = viewState.character.name,
                        status = viewState.character.status
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Data points
                items(viewState.characterDataPoints) {
                    Spacer(modifier = Modifier.height(32.dp))
                    DataPointComponent(dataPoint = it)
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
                // Button
                item {
                    Column {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Count of episodes: ${viewState.character.episodeIds.size}",
                        )
                        Text(
                            text = "View all episodes",
                            color = RickAction,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onEpisodeClicked(characterId)
                                }
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(64.dp)) }
            }

            else -> {}
        }


    }
}

@Composable
fun LoadingState() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 128.dp),
        color = MaterialTheme.colorScheme.primary
    )
}