package com.dipdev.themutemaster.data.local

data class SoundProfile(
    val ringerMode: Int?, // null means don't change
    val muteMedia: Boolean,
    val customMediaVolumePercent: Int? // 0-100, null means just use muteMedia
)
