package orlo.ricknmorty.ricknmorty.presentation.components.character

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import orlo.ricknmorty.network.models.domain.CharacterStatus
import orlo.ricknmorty.ricknmorty.ui.theme.RickNMortyTheme
import orlo.ricknmorty.ricknmorty.utils.asColor

@Composable
fun CharacterStatusComponent(characterStatus: CharacterStatus) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${characterStatus.displayName}",
                fontSize = 20.sp,
                color = characterStatus.asColor()
            )
        }
}

@Preview
@Composable
fun CharacterStatusComponentPreviewAlive() {
    RickNMortyTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Alive)
    }
}

@Preview
@Composable
fun CharacterStatusComponentPreviewDead() {
    RickNMortyTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Dead)
    }
}

@Preview
@Composable
fun CharacterStatusComponentPreviewUnknown() {
    RickNMortyTheme {
        CharacterStatusComponent(characterStatus = CharacterStatus.Unknown)
    }
}