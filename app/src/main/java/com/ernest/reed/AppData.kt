package com.ernest.reed

object AppData {
    const val currentUsername = "you"
    const val currentDisplayName = "Paulinus Ernest"

    val posts = mutableListOf(
        Post("ernest", "2h", "Discipline is the real freedom.", 1200, 84, 230),
        Post("chriskay", "2h", "Better ideas. Better days.", 856, 42, 97)
    )
    val reels = mutableListOf<Reel>()
}
