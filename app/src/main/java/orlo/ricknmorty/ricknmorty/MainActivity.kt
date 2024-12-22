package orlo.ricknmorty.ricknmorty

import CharacterDetailsScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import orlo.ricknmorty.network.Character
import orlo.ricknmorty.network.KtorClient
import orlo.ricknmorty.network.models.domain.CharacterStatus
import orlo.ricknmorty.ricknmorty.presentation.screens.CharacterEpisodeScreen
import orlo.ricknmorty.ricknmorty.ui.theme.RickNMortyTheme
import orlo.ricknmorty.ricknmorty.utils.asColor

class MainActivity : ComponentActivity() {

    private val ktorClient = KtorClient()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            RickNMortyTheme {
                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding) // Учитываем стандартные отступы от Scaffold
                            .padding(top = 16.dp) // Добавляем кастомный отступ сверху
                    ) {
                        NavHost(navController = navController, startDestination = "character_detail") {
                            composable("character_detail") {
                                CharacterDetailsScreen(
                                    ktorClient = ktorClient,
                                    characterId = 1
                                ) {
                                    navController.navigate("character_episodes/$it")
                                }
                            }
                            composable( route = "character_episodes/{characterId}",
                                arguments = listOf(navArgument("characterId") {
                                    type = NavType.IntType
                                })) { backStackEntry ->

                                val characterId: Int =
                                    backStackEntry.arguments?.getInt("characterId") ?: -1

                                CharacterEpisodeScreen(
                                    characterId = characterId,
                                    ktorClient = ktorClient
                                )
                            }
                        }

                    }

                }
            }
        }
    }


    @Composable
    fun CharacterStatusComponent(characterStatus: CharacterStatus) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .background(color = Color.LightGray, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 2.dp,
                    color = characterStatus.asColor(),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(top = 12.dp, bottom = 12.dp, start = 12.dp, end = 48.dp)
        ) {

            Text(text = "Status", fontSize = 14.sp)
            Text(text = characterStatus.displayName, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        }
    }

}

