package com.wemaka.charactersrm.presetation.characters.component

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wemaka.charactersrm.R
import com.wemaka.charactersrm.core.util.CharacterStatus
import com.wemaka.charactersrm.domain.util.CharacterFilters
import com.wemaka.charactersrm.domain.util.CharacterGender

@Composable
fun FiltersMenu(
    modifier: Modifier = Modifier,
    filters: CharacterFilters,
    onFiltersChange: (CharacterFilters) -> Unit,
    verticalPadding: Dp = 10.dp
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        DefaultTextField(
            hint = "Species",
            onChange = {
                onFiltersChange(filters.copy(species = it))
            }
        )

        DefaultTextField(
            hint = "Type",
            onChange = {
                onFiltersChange(filters.copy(type = it))
            }
        )

        BasicDropdownMenu(
            hint = "Status"
        ) {
            FilterChips(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                entries = CharacterStatus.entries,
                current = filters.status,
                textMapper = { it.name },
                onSelect = { onFiltersChange(filters.copy(status = it)) },
                horizontalPadding = 4.dp,
                verticalPadding = 4.dp
            )
        }

        BasicDropdownMenu(
            hint = "Gender"
        ) {
            FilterChips(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                entries = CharacterGender.entries,
                current = filters.gender,
                textMapper = { it.name },
                onSelect = { onFiltersChange(filters.copy(gender = it)) },
                horizontalPadding = 4.dp,
                verticalPadding = 4.dp
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = verticalPadding / 2)
                .heightIn(min = 12.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) { onFiltersChange(CharacterFilters()) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "reset",
                    lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Icon(
                    painter = painterResource(R.drawable.ic_close_small),
                    contentDescription = "reset filters",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun <T> FilterChips(
    modifier: Modifier = Modifier,
    entries: List<T>,
    current: T?,
    textMapper: (T) -> String = { it.toString() },
    onSelect: (T?) -> Unit,
    horizontalPadding: Dp = 8.dp,
    verticalPadding: Dp = 8.dp
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        entries.forEach { entry ->
            val selected = entry == current

            key(entry) {
                DefaultRadioButton(
                    selected = selected,
                    text = textMapper(entry).lowercase(),
                    onSelect = {
                        onSelect(if (selected) null else entry)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewFiltersMenu() {
    FiltersMenu(
        filters = CharacterFilters(),
        onFiltersChange = {}
    )
}