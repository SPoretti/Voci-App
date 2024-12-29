package com.example.vociapp.ui.components.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mapbox.search.ServiceProvider
import com.mapbox.search.common.CompletionCallback
import com.mapbox.search.record.HistoryRecord
import com.mapbox.search.ui.view.SearchResultAdapterItem
import com.mapbox.search.ui.view.UiError
import com.mapbox.search.ui.view.SearchResultsView

@Composable
fun ShowSearchHistory() {
    val historyDataProvider = ServiceProvider.INSTANCE.historyDataProvider()
    val context = LocalContext.current
    val searchResultsView = SearchResultsView(context)

    // Show `loading` item that indicates the progress of `search history` loading operation.
    searchResultsView.setAdapterItems(listOf(SearchResultAdapterItem.Loading))

    // Load `search history`
    var loadingTask = historyDataProvider.getAll(object : CompletionCallback<List<HistoryRecord>> {
        override fun onComplete(result: List<HistoryRecord>) {
            val viewItems = mutableListOf<SearchResultAdapterItem>().apply {
                // Add `Recent searches` header
                add(SearchResultAdapterItem.RecentSearchesHeader)

                // Add history record items
                addAll(result.map { history ->
                    SearchResultAdapterItem.History(
                        history,
                        isFavorite = false
                    )
                })
            }

            // Show prepared items
            searchResultsView.setAdapterItems(viewItems)
        }

        override fun onError(e: Exception) {
            // Show error in case of failure
            val errorItem = SearchResultAdapterItem.Error(UiError.createFromException(e))
            searchResultsView.setAdapterItems(listOf(errorItem))
        }
    })
}