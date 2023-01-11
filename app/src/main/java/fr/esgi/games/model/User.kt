package fr.esgi.games.model

data class User(
    val id: String,
    val email: String,
    val liked: List<String>,
    val wished: List<String>)
{}