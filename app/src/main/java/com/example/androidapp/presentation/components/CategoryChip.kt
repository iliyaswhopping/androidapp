package com.example.androidapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidapp.presentation.theme.AndroidappTheme
@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryChipPreview() {
    AndroidappTheme {
        Row {
            CategoryChip(label = "All", selected = true, onClick = {})
            CategoryChip(label = "Languages", selected = false, onClick = {})
        }
    }
}
