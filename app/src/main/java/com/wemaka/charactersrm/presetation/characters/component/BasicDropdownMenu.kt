package com.wemaka.charactersrm.presetation.characters.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wemaka.charactersrm.presetation.ui.theme.backgroundDark
import com.wemaka.charactersrm.presetation.ui.theme.surfaceDark
import com.wemaka.charactersrm.presetation.ui.theme.textSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDropdownMenu(
    hint: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.wrapContentSize(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = surfaceDark,
                unfocusedContainerColor = surfaceDark
            ),
            placeholder = {
                Text(
                    text = hint,
                    color = textSecondary,
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            content = content
        )
    }
}

@Composable
@Preview
fun PreviewBasicDropdownMenu() {
    BasicDropdownMenu(
        hint = "Text",
        modifier = Modifier.size(400.dp)
    ) {
        Column {
            Text("text 1")
            Text("text 2")
            Text("text 3")
        }
    }
}