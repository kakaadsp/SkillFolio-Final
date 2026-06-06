package com.example.skillfoliofinal

/**
 * Data class yang merepresentasikan tabel 'users' di SQLite.
 *
 * Field tambahan (bio, lokasi, linkedin, github, fotoUri) memungkinkan
 * fitur Kelengkapan Profil tanpa perlu tabel terpisah.
 */
data class User(
    val id: Int = 0,
    val nama: String,
    val email: String,
    val phone: String = "",
    val prodi: String = "",
    val universitas: String = "",
    val password: String,
    val bio: String = "",
    val lokasi: String = "",
    val linkedin: String = "",
    val github: String = "",
    val fotoUri: String = ""
)
