package com.example.temaaplicatiimobile

import android.content.Intent
import com.firebase.ui.auth.AuthUI

class SigninIntentBuilder {
    fun createSignInIntent(): Intent {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and return sign-in intent
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.drawable.ic_launcher_background) // @TODO set logo
            .setTheme(R.style.Theme_Temaaplicatiimobile) // Set theme
            .setAvailableProviders(providers)
            .build()
        // [END auth_fui_create_intent]
    }
}