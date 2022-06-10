package com.example.temaaplicatiimobile

import android.content.Intent
import com.firebase.ui.auth.AuthUI

class SigninIntentBuilder {
    fun createSignInIntent(): Intent {

        //Construiere ecran de login/register
        //folosind o lista de optiuni - email, google
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.drawable.ic_launcher_background) // @TODO set logo
            .setTheme(R.style.Theme_Temaaplicatiimobile) // Set theme
            .setAvailableProviders(providers)
            .build()
    }
}