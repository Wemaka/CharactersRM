package com.wemaka.charactersrm.presetation.characters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.SubcomposeAsyncImage
import com.wemaka.charactersrm.R
import com.wemaka.charactersrm.core.component.ErrorBox
import com.wemaka.charactersrm.core.util.CharacterStatus
import com.wemaka.charactersrm.domain.model.CharacterItem
import com.wemaka.charactersrm.domain.util.CharacterFilters
import com.wemaka.charactersrm.presetation.characters.component.CircleStatus
import com.wemaka.charactersrm.presetation.characters.component.FiltersMenu
import com.wemaka.charactersrm.presetation.ui.theme.backgroundDark
import com.wemaka.charactersrm.presetation.ui.theme.surfaceDark
import com.wemaka.charactersrm.presetation.ui.theme.textPrimary
import com.wemaka.charactersrm.presetation.ui.theme.textSecondary
import com.wemaka.charactersrm.presetation.util.Screen
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun CharactersScreen(
    navController: NavController,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    CharactersContent(
        navController = navController,
        onEvent = viewModel::onEvent,
        state = viewModel.state
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CharactersContent(
    navController: NavController,
    onEvent: (CharactersEvent) -> Unit,
    state: CharactersState
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val shouldLoadMore = remember(state.isLoading, state.isEndReached) {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo
                .totalItemsCount - 1 &&
                    !state.isEndReached &&
                    !state.isLoading
        }
    }
    val topState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topState)

    if (
        topState.overlappedFraction >= 1f &&
        topState.collapsedFraction >= 1f &&
        topState.heightOffset <= topState.heightOffsetLimit &&
        state.isFiltersSectionVisible
    ) {
        onEvent(CharactersEvent.ToggleFiltersSection)
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onEvent(CharactersEvent.LoadCharacters)
        }
    }

    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopSearchBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                filters = state.charactersFilters,
                onAction = { onEvent(CharactersEvent.ToggleFiltersSection) },
                onFilters = { onEvent(CharactersEvent.Filters(it)) },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isAtTop,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                ),
                exit = scaleOut(
                    targetScale = 0.8f,
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                ) + fadeOut(animationSpec = MaterialTheme.motionScheme.fastSpatialSpec())
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.scrollToItem(0)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_up),
                        contentDescription = "Scroll to top"
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                state = rememberPullToRefreshState(),
                isRefreshing = isRefreshing,
                onRefresh = { onEvent(CharactersEvent.RefreshLoadCharacters) }
            ) {
                CharacterGrid(
                    modifier = Modifier.padding(20.dp),
                    list = state.charactersList,
                    state = listState,
                    navController = navController
                )

                if (state.charactersList.isEmpty()) {
                    ErrorBox(
                        isLoading = state.isLoading,
                        loadError = state.loadError
                    )
                }
            }

            AnimatedVisibility(
                visible = state.isFiltersSectionVisible,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                )
            ) {
                FiltersMenu(
                    modifier = Modifier
                        .background(color = backgroundDark)
                        .padding(20.dp),
                    filters = state.charactersFilters,
                    onFiltersChange = { onEvent(CharactersEvent.Filters(it)) },
                    verticalPadding = 15.dp
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    filters: CharacterFilters,
    onAction: (CharactersEvent) -> Unit = {},
    onFilters: (CharacterFilters) -> Unit
) {
    val textFieldState = rememberTextFieldState("")
    val searchBarState = rememberSearchBarState()
    var rotated by remember { mutableStateOf(false) }

    Column {
        TopAppBar(
            modifier = modifier,
            expandedHeight = 0.dp,
            title = {
                SearchBar(
                    tonalElevation = 0.dp,
                    modifier = Modifier,
                    state = searchBarState,
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = textFieldState.text.toString(),
                            onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                            onSearch = {
                                onFilters(filters.copy(name = textFieldState.text.toString()))
                            },
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = {
                                Text(
                                    text = "Search characters...",
                                    color = textSecondary
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            )
                        )
                    },
                    shape = SearchBarDefaults.fullScreenShape,
                    colors = SearchBarDefaults.colors(
                        containerColor = backgroundDark
                    )
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        rotated = !rotated
                        onAction(CharactersEvent.ToggleFiltersSection)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .rotate(
                                animateFloatAsState(
                                    targetValue = if (rotated) 180f else 0f,
                                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                                ).value
                            ),
                        painter = painterResource(R.drawable.ic_keyboard_arrow_down),
                        contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundDark,
                scrolledContainerColor = backgroundDark
            ),
        )
    }
}

@Composable
fun CharacterGrid(
    modifier: Modifier = Modifier,
    list: List<CharacterItem>,
    state: LazyListState,
    navController: NavController,
    gap: Dp = 20.dp
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state
    ) {
        val rowCount = if (list.size % 2 == 0) list.size / 2 else list.size / 2 + 1

        items(
            items = list
        ) {

        }

        items(
            count = rowCount,
            key = { index ->
                val firstId = list.getOrNull(index * 2)?.id ?: 0
                val secondId = list.getOrNull(index * 2 + 1)?.id ?: 0
                "$firstId-$secondId"
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                CharacterCard(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    item = list[it * 2]
                )

                if (it * 2 + 1 >= list.size) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    CharacterCard(
                        modifier = Modifier.weight(1f),
                        navController = navController,
                        item = list[it * 2 + 1]
                    )
                }
            }

            Spacer(modifier = Modifier.height(gap))
        }
    }
}

@Composable
fun CharacterCard(
    navController: NavController,
    modifier: Modifier = Modifier,
    item: CharacterItem
) {
    Card(
        modifier = modifier
            .aspectRatio(0.7f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = {
            navController.navigate("${Screen.CharacterScreen.route}/${item.id}")
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomEnd
            ) {
                SubcomposeAsyncImage(
                    model = item.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(20.dp)
                        )
                    },
                    error = {
                        Image(
                            painter = painterResource(R.drawable.ic_placeholder),
                            contentDescription = "Error loading",
                            contentScale = ContentScale.Fit
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .background(
                            color = backgroundDark,
                            shape = RoundedCornerShape(topStart = 15.dp)
                        )
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .heightIn(min = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(6.dp))
                    CircleStatus(
                        isActive = item.status.lowercase() == CharacterStatus.ALIVE.toString().lowercase(),
                        size = 8.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        textAlign = TextAlign.Right,
                        text = item.status,
                        color = textSecondary,
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp)
                    .background(surfaceDark)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = item.name,
                    color = textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "${item.gender} | ${item.species}",
                    color = textSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewCharactersContent() {
    CharactersContent(
        navController = rememberNavController(),
        {},
        CharactersState(
            charactersList = listOf(
                CharacterItem(1, "Rick", "Maleeeeeeeeee", "", "Alive", "Human", ""),
                CharacterItem(2, "Riiiiiiiiiiiiiiiiiiiiiiiiick", "Male", "", "Alive", "Human", ""),
                CharacterItem(3, "Rick", "Maleeeeeeeeee", "", "Alive", "Human", "")
            )
        )
    )
}
