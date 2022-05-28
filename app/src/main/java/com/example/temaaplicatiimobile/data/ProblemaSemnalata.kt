package com.example.temaaplicatiimobile.data

import com.google.firebase.auth.FirebaseUser
import java.util.*

data class ProblemaSemnalata(
    val uniqueId: UUID,
    val userId: String,
    val imagine: String,
    val descriere: String

)
