package com.wemaka.charactersrm.presetation.characters.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.RadioButtonDefaults
import androidx.wear.compose.material3.Text

@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit
) {
    RadioButton(
        modifier = modifier.heightIn(min = 12.dp),
        selected = selected,
        onSelect = onSelect,
        colors = RadioButtonDefaults.radioButtonColors(),
        label = { Text(text = text) },
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
@Preview
fun PreviewDefaultRadioButton() {
    DefaultRadioButton(
        "Text", false
    ) { }
}