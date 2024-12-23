package orlo.ricknmorty.ricknmorty.presentation.screens.Character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import orlo.ricknmorty.network.models.domain.Character
import orlo.ricknmorty.ricknmorty.presentation.components.character.CharacterGridItem
import orlo.ricknmorty.ricknmorty.presentation.viewmodels.CharactersScreenViewModel
import orlo.ricknmorty.ricknmorty.utils.ErrorState
import orlo.ricknmorty.ricknmorty.utils.LoadingState

sealed interface CharacterScreenViewState {
    object Loading : CharacterScreenViewState
    data class Error(val message: String) : CharacterScreenViewState
    data class GridDisplay(
        val characters: List<Character> = emptyList()
    ) : CharacterScreenViewState
}

@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersScreenViewModel = hiltViewModel()
) {

    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(key1 = Unit, block = { viewModel.fetchInitialPage() })

    val scrollState = rememberLazyGridState()
    val fetchNextPage: Boolean by remember {
        derivedStateOf {
            val currentCharacterCount =
                (viewState as? CharacterScreenViewState.GridDisplay)?.characters?.size
                    ?: return@derivedStateOf false
            val lastDisplayedIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            return@derivedStateOf lastDisplayedIndex >= currentCharacterCount - 10
        }
    }

    LaunchedEffect(key1 = fetchNextPage, block = {
        if (fetchNextPage) viewModel.fetchNextPage()
    })


    when (val state = viewState) {
        CharacterScreenViewState.Loading -> LoadingState()
        is CharacterScreenViewState.GridDisplay -> {
            LazyVerticalGrid(
                state = scrollState,
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                columns = GridCells.Fixed(2),
                content = {
                    items(
                        items = state.characters,
                        key = { it.id }
                    ) { character ->
                        CharacterGridItem(modifier = Modifier, character = character) {
                            onCharacterClick(character.id)
                        }
                    }
                })
        }

        is CharacterScreenViewState.Error -> ErrorState()
    }
}