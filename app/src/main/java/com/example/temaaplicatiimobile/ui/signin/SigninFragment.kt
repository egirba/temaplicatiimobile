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

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        Log.d("logIn", response?.email.toString());
        this.view?.let { refresh(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signInButton = view.findViewById<View>(R.id.button_sign_in) as Button
        var loguoutButton = view.findViewById<View>(R.id.button_logout) as Button
        loguoutButton.setOnClickListener {
            this.context?.let { it1 ->
                AuthUI.getInstance()
                    .signOut(it1)
                    .addOnCompleteListener {
                        this.refresh(view);
                    }
            }
        }
        signInButton.setOnClickListener {
            signInLauncher.launch(SigninIntentBuilder().createSignInIntent());
            Log.d("btnSetup", "Selected")
        }
        refresh(view);

    }

    private fun refresh(view: View) {
        val firebaseAuth = FirebaseAuth.getInstance();
        val user = firebaseAuth.currentUser;
        Log.d("profile", user?.uid.toString())
        Log.d("profile", user?.email.toString())
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