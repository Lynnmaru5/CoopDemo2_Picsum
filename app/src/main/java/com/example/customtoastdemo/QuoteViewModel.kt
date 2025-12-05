package com.example.customtoastdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// UI state for the quote section
data class QuoteUiState(
    val loading: Boolean = false,
    val quote: String? = null,
    val character: String? = null,
    val anime: String? = null
)

class QuoteViewModel(private val repo: QuoteRepository) : ViewModel() {

    private val _ui = MutableStateFlow(QuoteUiState())
    val uiState = _ui.asStateFlow()

    // Loads quote instantly (no API)
    fun fetchQuote() {
        _ui.value = QuoteUiState(loading = true)

        val q = repo.getRandomQuote()

        _ui.value = QuoteUiState(
            loading = false,
            quote = q.quote,
            character = q.character,
            anime = q.anime
        )
    }

    companion object {
        fun factory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val repo = QuoteRepository()
                    @Suppress("UNCHECKED_CAST")
                    return QuoteViewModel(repo) as T
                }
            }
    }
}
