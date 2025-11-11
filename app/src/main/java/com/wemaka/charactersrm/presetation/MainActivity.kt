package com.wemaka.charactersrm.presetation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wemaka.charactersrm.presetation.character.CharacterScreen
import com.wemaka.charactersrm.presetation.characters.CharactersScreen
import com.wemaka.charactersrm.presetation.ui.theme.CharactersRMTheme
import com.wemaka.charactersrm.presetation.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharactersRMTheme {
                Surface {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.CharactersScreen.route
                    ) {
                        composable(Screen.CharactersScreen.route) {
                            CharactersScreen(navController = navController)
                        }
                        composable(
                            route = "${Screen.CharacterScreen.route}/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            val id = remember { it.arguments?.getInt("id") ?: 0 }
                            CharacterScreen(
                                navController = navController,
                                characterId = id
                            )
                        }
                    }
                }
            }
        }
    }
}