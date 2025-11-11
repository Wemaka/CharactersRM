package com.wemaka.charactersrm.presetation.character

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.Text
import coil3.asDrawable
import coil3.compose.SubcomposeAsyncImage
import com.wemaka.charactersrm.R
import com.wemaka.charactersrm.core.util.CharacterStatus
import com.wemaka.charactersrm.domain.model.Character
import com.wemaka.charactersrm.domain.model.Location
import com.wemaka.charactersrm.domain.model.Origin
import com.wemaka.charactersrm.presetation.characters.component.CircleStatus
import com.wemaka.charactersrm.presetation.ui.theme.backgroundDark
import com.wemaka.charactersrm.presetation.ui.theme.surfaceDark
import com.wemaka.charactersrm.presetation.ui.theme.textPrimary
import com.wemaka.charactersrm.presetation.ui.theme.textSecondary
import androidx.compose.ui.platform.LocalResources
import com.wemaka.charactersrm.core.component.ErrorBox

@Composable
fun CharacterScreen(
    navController: NavController,
    viewModel: CharacterViewModel = hiltViewModel(),
    characterId: Int
) {
    CharacterContent(
        navController = navController,
        onEvent = viewModel::onEvent,
        getColors = viewModel::calcDominantColor,
        state = viewModel.state,
        characterId = characterId
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterContent(
    navController: NavController,
    onEvent: (CharacterEvent) -> Unit,
    getColors: (Drawable, (Color) -> Unit) -> Unit,
    state: CharacterState,
    characterId: Int
) {
    val character = state.character
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val defaultDominantColor = backgroundDark
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    LaunchedEffect(Unit) {
        onEvent(CharacterEvent.LoadCharacter(characterId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Character",
                        color = textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundDark
                ),
                scrollBehavior = null
            )
        }
    ) { padding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = refreshState,
            isRefreshing = isRefreshing,
            onRefresh = { onEvent(CharacterEvent.LoadCharacter(characterId)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(defaultDominantColor, dominantColor.copy(alpha = 0.5f))
                        )
                    )
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (character != null) {
                    CharacterCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        successImage = { image ->
                            getColors(image) { newColor ->
                                dominantColor = newColor
                            }
                        },
                        name = character?.name,
                        image = character?.image,
                        status = character?.status,
                        species = character?.species,
                        gender = character?.gender,
                        type = character?.type
                    )

                    InfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        originName = character?.origin?.name,
                        locationName = character?.location?.name
                    )

                    EpisodesList(
                        episodeUrls = character?.episode ?: emptyList()
                    )
                }

                ErrorBox(
                    isLoading = state.isLoading,
                    loadError = state.loadError
                )
            }
        }
    }
}

@Composable
fun CharacterCard(
    modifier: Modifier = Modifier,
    successImage: (Drawable) -> Unit = {},
    name: String?,
    image: String?,
    status: String?,
    species: String?,
    gender: String?,
    type: String?
) {
    val resource = LocalResources.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(20.dp)
                    )
                },
                onSuccess = { state ->
                    successImage(state.result.image.asDrawable(resource))
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(surfaceDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Image failed",
                            color = textSecondary
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xAA000000)),
                            startY = 150f
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .background(
                            color = backgroundDark,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleStatus(
                        isActive = status?.lowercase() == CharacterStatus.ALIVE.toString()
                            .lowercase(),
                        size = 8.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$status • $species",
                        color = textPrimary,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(2.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .background(
                            color = backgroundDark,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = name.toString(),
                        color = textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        textAlign = TextAlign.Left,
                        text = gender.toString() + (if (type?.isNotBlank() == true) " • $type" else ""),
                        color = textSecondary,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    originName: String?,
    locationName: String?
) {
    Column(
        modifier = modifier
            .background(
                color = surfaceDark,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        InfoRow(
            painter = painterResource(R.drawable.ic_home_place),
            label = "Origin",
            value = originName.toString()
        )
        Spacer(modifier = Modifier.height(4.dp))
        InfoRow(
            painter = painterResource(R.drawable.ic_place),
            label = "Location",
            value = locationName.toString()
        )
    }
}

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    label: String,
    value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            tint = Color.White,
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                color = textSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value.toString()
            )
        }
    }
}

@Composable
fun EpisodesList(
    modifier: Modifier = Modifier,
    episodeUrls: List<String>
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Episodes",
                color = textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = "${episodeUrls.size}",
                color = textSecondary,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(episodeUrls) { index, epUrl ->
                Box(
                    modifier = Modifier
                        .background(
                            color = surfaceDark,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = epUrl.substringAfterLast('/').takeIf { it.isNotBlank() } ?: epUrl,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

}

@Composable
@Preview
fun PreviewCharacterContent() {
    CharacterContent(
        navController = rememberNavController(),
        onEvent = {},
        getColors = {} as (Drawable, (Color) -> Unit) -> Unit,
        state = CharacterState(
            isLoading = false,
            character = Character(
                id = 1,
                name = "Rick",
                gender = "Male",
                image = "",
                status = "Alive",
                created = "",
                episode = List(10) { "/${it + 1}" },
                location = Location("Citadel of Ricks", ""),
                origin = Origin("unknown", ""),
                species = "Human",
                type = "Rick's Toxic",
                url = ""
            ),
            loadError = ""
        ),
        characterId = 1
    )
}