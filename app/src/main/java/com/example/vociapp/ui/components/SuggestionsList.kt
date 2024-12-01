package com.example.vociapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SuggestionList(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    LazyColumn {
        items(suggestions) { suggestion ->
            Text(
                text = suggestion,
                modifier = Modifier
                    .clickable { onSuggestionClick(suggestion) }
                    .padding(8.dp)
            )
        }
    }
}