package com.audcode.audio

import com.audcode.ui.home.model.EpisodeModel

sealed class PlayerState {
    data class Other(val episode: EpisodeModel? = null) : PlayerState()
    data class Playing(val episode: EpisodeModel) : PlayerState()
    data class Paused(val episode: EpisodeModel) : PlayerState()
    data class Ended(val episode: EpisodeModel) : PlayerState()
    data class Error(val episode: EpisodeModel, val exception: Exception?) : PlayerState()
}