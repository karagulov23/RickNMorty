package orlo.ricknmorty.ricknmorty

import orlo.ricknmorty.ricknmorty.presentation.screens.Character.CharacterDetailsScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import orlo.ricknmorty.network.KtorClient
import orlo.ricknmorty.network.models.domain.CharacterStatus
import orlo.ricknmorty.ricknmorty.presentation.screens.Character.CharacterEpisodeScreen
import orlo.ricknmorty.ricknmorty.presentation.screens.Character.CharactersScreen
import orlo.ricknmorty.ricknmorty.presentation.screens.Episode.AllEpisodeScreen
import orlo.ricknmorty.ricknmorty.presentation.ui.theme.RickNMortyTheme
import orlo.ricknmorty.ricknmorty.utils.asColor

sealed class NavDestination(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    object CharactersScreen : NavDestination(
        title = "Characters",
        route = "characters_screen",
        icon = Icons.Filled.Person
    )

    object EpisodesScreen :
        NavDestination(title = "Episodes", route = "episodes_screen", icon = Icons.Filled.PlayArrow)

    object LocationsScreen :
        NavDestination(title = "Locations", route = "locations_screen", icon = Icons.Filled.Place)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val ktorClient = KtorClient()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val items = listOf(
                NavDestination.CharactersScreen,
                NavDestination.EpisodesScreen,
                NavDestination.LocationsScreen
            )

            RickNMortyTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { topLevelRoute ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            topLevelRoute.icon,
                                            contentDescription = topLevelRoute.title
                                        )
                                    },
                                    label = { Text(topLevelRoute.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == topLevelRoute.route } == true,
                                    onClick = {
                                        navController.navigate(topLevelRoute.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = "characters_screen",
                        Modifier.padding(innerPadding)
                    ) {
                        composable(route = "characters_screen") {
                            CharactersScreen(onCharacterClick = { characterId ->
                                navController.navigate("character_detail/$characterId")
                            })
                        }
                        composable(
                            route = "character_detail/{characterId}",
                            arguments = listOf(navArgument("characterId") {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val characterId: Int =
                                backStackEntry.arguments?.getInt("characterId") ?: -1
                            CharacterDetailsScreen(characterId = characterId) {
                                navController.navigate("character_episodes/$it")
                            }
                        }
                        composable(
                            route = "character_episodes/{characterId}",
                            arguments = listOf(navArgument("characterId") {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val characterId: Int =
                                backStackEntry.arguments?.getInt("characterId") ?: -1
                            CharacterEpisodeScreen(
                                characterId = characterId,
                                ktorClient = ktorClient
                            )
                        }
                        composable(route = NavDestination.EpisodesScreen.route) {
                            AllEpisodeScreen()
                        }
                        composable(route = NavDestination.LocationsScreen.route) {
                            Text(text = "Locations")
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



