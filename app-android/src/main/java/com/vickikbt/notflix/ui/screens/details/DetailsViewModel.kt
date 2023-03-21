package com.vickikbt.notflix.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vickikbt.shared.domain.models.MovieDetails
import com.vickikbt.shared.domain.repositories.MovieDetailsRepository
import com.vickikbt.shared.utils.DetailsUiState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) : ViewModel() {

    private val _movieDetailsState = MutableStateFlow(DetailsUiState(isLoading = true))
    val movieDetailsState = _movieDetailsState.asStateFlow()

    fun getMovieDetails(movieId: Int) = viewModelScope.launch {
        movieDetailsRepository.fetchMovieDetails(movieId = movieId).collect { movieDetailsResult ->
            movieDetailsResult.onSuccess { movieDetails ->
                _movieDetailsState.update {
                    it.copy(movieDetails = movieDetails, isLoading = false)
                }
            }.onFailure { error ->
                _movieDetailsState.update {
                    it.copy(error = error.localizedMessage, isLoading = false)
                }
            }
        }
    }

    fun getMovieCast(movieId: Int) = viewModelScope.launch {
        movieDetailsRepository.fetchMovieCast(movieId = movieId).collect { movieCastsResult ->
            movieCastsResult.onSuccess { cast ->
                _movieDetailsState.update {
                    it.copy(movieCast = cast.actor, isLoading = false)
                }
            }.onFailure { error ->
                _movieDetailsState.update {
                    it.copy(error = error.localizedMessage, isLoading = false)
                }
            }
        }
    }

    fun fetchSimilarMovies(movieId: Int) = viewModelScope.launch {
        movieDetailsRepository.fetchSimilarMovies(movieId).collect { moviesResult ->
            moviesResult.onSuccess { similarMovies ->
                _movieDetailsState.update {
                    it.copy(similarMovies = similarMovies, isLoading = false)
                }
            }.onFailure { error ->
                _movieDetailsState.update {
                    it.copy(
                        error = error.localizedMessage, isLoading = false
                    )
                }
            }
        }
    }

    @Deprecated("Pending caching implementation")
    fun saveMovieDetails(movieDetails: MovieDetails) = viewModelScope.launch {
        try {
            movieDetailsRepository.apply {
                // movieDetailsRepository.saveMovieDetails(movieDetail = movieDetails)
            }
        } catch (e: Exception) {
            Napier.e("Error saving movie: $e")
        }
    }

    @Deprecated("Pending caching implementation")
    fun deleteFavouriteMovie(movieId: Int) = viewModelScope.launch {
        // favouritesPresenter.deleteFavouriteMovie(movieId = movieId)
    }
}
