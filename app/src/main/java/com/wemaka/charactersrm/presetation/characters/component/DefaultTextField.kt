package com.wemaka.charactersrm.presetation.characters.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text
import com.wemaka.charactersrm.presetation.ui.theme.backgroundDark
import com.wemaka.charactersrm.presetation.ui.theme.surfaceDark
import com.wemaka.charactersrm.presetation.ui.theme.textPrimary
import com.wemaka.charactersrm.presetation.ui.theme.textSecondary

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    hint: String = "",
    onChange: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }

    Box(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = surfaceDark,
                unfocusedContainerColor = surfaceDark,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = text,
            onValueChange = {
                text = it
                onChange(it)
            },
            maxLines = 1,
            singleLine = true,
            placeholder = {
                Text(
                    text = hint,
                    color = textSecondary
                )
            },
            shape = CircleShape
        )
    }
}

@Composable
@Preview
fun PreviewDefaultTextField() {
    DefaultTextField(
        hint = "Text..."
    )
}