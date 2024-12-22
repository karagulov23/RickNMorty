package orlo.ricknmorty.ricknmorty.presentation.components.episode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeasonHeaderComponent(seasonNumber: Int) {
    Text(
        text = "Season $seasonNumber",
        color = MaterialTheme.colorScheme.primary,
        fontSize = 32.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(8.dp)
    )

}