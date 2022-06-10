package com.example.temaaplicatiimobile.ui.signin


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.temaaplicatiimobile.R
import com.example.temaaplicatiimobile.SigninIntentBuilder
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [SigninFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SigninFragment : Fragment() {

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.view?.let { refresh(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setare actiune pe butonul de logout
        var logoutButton = view.findViewById<View>(R.id.button_logout) as Button
        logoutButton.setOnClickListener {
            this.context?.let { it1 ->
                AuthUI.getInstance()
                    .signOut(it1)
                    .addOnCompleteListener {
                        this.refresh(view);
                    }
            }
        }

        //setare actiune pe butonul de signin
        val signInButton = view.findViewById<View>(R.id.button_sign_in) as Button
        signInButton.setOnClickListener {
            // la click pe signin, se va construi un SignInIntent
            var signinIntent = SigninIntentBuilder().createSignInIntent();
            // se lanseaza SignInIntent, care face parte din libraria Firebase Auth
            signInLauncher.launch(signinIntent);
        }
        refresh(view);

    }

    private fun refresh(view: View) {
        val firebaseAuth = FirebaseAuth.getInstance();
        val user = firebaseAuth.currentUser;
        val signInButton = view.findViewById<View>(R.id.button_sign_in) as Button
        var loguoutButton = view.findViewById<View>(R.id.button_logout) as Button
        var profileDetails = view.findViewById<View>(R.id.profile_details) as TextView
        if (user != null) {
            profileDetails.text = user.email;
            signInButton?.isEnabled = false
            loguoutButton?.isEnabled = true
        } else {
            profileDetails.text = "";
            signInButton?.isEnabled = true
            loguoutButton?.isEnabled = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SigninFragment().apply {

            }
    }
}