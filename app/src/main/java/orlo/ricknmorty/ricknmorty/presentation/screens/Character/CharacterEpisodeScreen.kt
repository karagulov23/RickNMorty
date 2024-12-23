package orlo.ricknmorty.ricknmorty.presentation.screens.Character

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import orlo.ricknmorty.network.KtorClient
import orlo.ricknmorty.network.models.domain.Character
import orlo.ricknmorty.network.models.domain.Episode
import orlo.ricknmorty.ricknmorty.presentation.components.character.CharacterDetailsNamePlateComponent
import orlo.ricknmorty.ricknmorty.presentation.components.episode.EpisodeRowComponent
import orlo.ricknmorty.ricknmorty.presentation.components.episode.SeasonHeaderComponent
import orlo.ricknmorty.ricknmorty.utils.LoadingState

@Composable
fun CharacterEpisodeScreen(
    characterId: Int,
    ktorClient: KtorClient
){
    var characterState by remember {
        mutableStateOf<Character?>(null)
    }
    var episodesState by remember {
        mutableStateOf<List<Episode>>(emptyList())
    }

    LaunchedEffect(key1 = Unit, block = {
        ktorClient.getCharacter(characterId).onSuccess { character ->
            characterState = character
            launch {
                ktorClient.getEpisodes(character.episodeIds).onSuccess  { episodes ->
                episodesState = episodes
                }.onFailure {

                }
            }
        }.onFailure {
            //
        }
    })

    characterState?.let {
        CharacterEpisodeScreen(character = it, episodes = episodesState)
    } ?: LoadingState()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharacterEpisodeScreen(character: Character, episodes: List<Episode>) {

    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }

   LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            CharacterDetailsNamePlateComponent(name = character.name, status = character.status)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        episodeBySeasonMap.forEach { mapEntry ->
            stickyHeader { SeasonHeaderComponent(seasonNumber = mapEntry.key) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(mapEntry.value) { episode ->
                EpisodeRowComponent(episode = episode)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}