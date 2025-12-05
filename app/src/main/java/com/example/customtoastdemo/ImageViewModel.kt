package com.example.customtoastdemo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI model for one photo shown in the screen
data class UiPhoto(
    val id: String,
    val author: String,
    val url: String,
    val downloadUrl: String
)

// Holds current UI state (filter, loading, errors, and photo)
data class UiState(
    val authorFilter: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val photo: UiPhoto? = null
)

// ViewModel: connects UI with repository (API + Room)
class ImageViewModel(private val repo: ImageRepository) : ViewModel() {

    // Internal mutable state
    private val _ui = MutableStateFlow(UiState())

    // Public read-only state observed by Compose
    val uiState = _ui.asStateFlow()

    // Updates the author filter when user types
    fun onAuthorChange(new: String) {
        _ui.value = _ui.value.copy(authorFilter = new)
    }

    // Fetch one image from API and save it to Room
    fun fetchOne() {
        viewModelScope.launch {
            try {
                // Show loading, clear previous error/photo
                _ui.value = _ui.value.copy(loading = true, error = null, photo = null)

                // Call Picsum API
                val list = repo.fetchPhotos(page = 1, limit = 30)

                // Apply local filter by author if not blank
                val filter = _ui.value.authorFilter.trim()
                val filtered = if (filter.isNotEmpty()) {
                    list.filter { it.author.contains(filter, ignoreCase = true) }
                } else {
                    list
                }

                // Take the first result or throw error if none
                val first = filtered.firstOrNull()
                    ?: throw IllegalStateException("No results. Try clearing the author filter.")

                // Save to Room for offline viewing
                repo.saveImageLocally(first)

                // Update UI with fetched photo
                _ui.value = _ui.value.copy(
                    loading = false,
                    photo = UiPhoto(
                        id = first.id,
                        author = first.author,
                        url = first.url,
                        downloadUrl = first.downloadUrl
                    )
                )
            } catch (t: Throwable) {
                // Show error message on screen
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = t.message ?: "Unknown error"
                )
            }
        }
    }

    // Load the last saved image from Room (if any)
    fun loadLastSaved() {
        viewModelScope.launch {
            try {
                // Show loading state
                _ui.value = _ui.value.copy(loading = true, error = null, photo = null)

                // Read from local DB
                val saved = repo.loadLastSavedImage()
                    ?: throw IllegalStateException("No saved image yet. Fetch one first.")

                // Update UI with locally stored image
                _ui.value = _ui.value.copy(
                    loading = false,
                    photo = UiPhoto(
                        id = "local",           // mark as local
                        author = saved.author,
                        url = saved.url,
                        downloadUrl = saved.url
                    )
                )
            } catch (t: Throwable) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = t.message ?: "Error loading saved image"
                )
            }
        }
    }

    companion object {
        // Factory: builds ImageViewModel with API + Room using a Context
        fun factory(appContext: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    // Create Retrofit service
                    val api = PicsumService.create()
                    // Get Room database + DAO
                    val db = AppDatabase.getDatabase(appContext)
                    val dao = db.imageDao()
                    // Build repository that knows API + Room
                    val repo = ImageRepository(api, dao)
                    @Suppress("UNCHECKED_CAST")
                    return ImageViewModel(repo) as T
                }
            }
    }
}
