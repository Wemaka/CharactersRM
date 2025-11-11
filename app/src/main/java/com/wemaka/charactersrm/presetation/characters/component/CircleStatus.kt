package com.wemaka.charactersrm.presetation.characters.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wemaka.charactersrm.presetation.ui.theme.errorRed
import com.wemaka.charactersrm.presetation.ui.theme.successGreen

@Composable
fun CircleStatus(
    isActive: Boolean = false,
    size: Dp = 10.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = if (isActive) successGreen else errorRed,
                shape = CircleShape)
    )
}

@Composable
@Preview
fun PreviewCircleStatus() {
    CircleStatus()
}