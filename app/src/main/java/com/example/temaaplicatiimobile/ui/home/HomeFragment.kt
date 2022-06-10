package com.example.temaaplicatiimobile.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.temaaplicatiimobile.R
import com.example.temaaplicatiimobile.SigninIntentBuilder
import com.example.temaaplicatiimobile.data.ProblemaSemnalata
import com.example.temaaplicatiimobile.databinding.FragmentHomeBinding
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.util.*


class HomeFragment() : Fragment() {
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.view?.let {
            Snackbar.make(
                it,
                R.string.sigin_success,
                Snackbar.LENGTH_SHORT
            ).show()
        }
        this.takePhoto();
    }

    private var LOG_TAG = "homeActivity"
    private var imageBitmap: Bitmap? = null
    private var imageUploadLauncherResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                //TODO show error
            }

            val extras: Bundle? = result.data?.extras;
            this.imageBitmap = extras?.get("data") as Bitmap?
            val uploadImagine = this.view?.findViewById<View>(R.id.upload_photo) as Button
            val imagePreview = this.view?.findViewById<View>(R.id.picture_preview) as ImageView
            val descriereProblema =
                this.view?.findViewById<View>(R.id.problem_description) as TextInputLayout
            imagePreview.setImageBitmap(imageBitmap)
            if (imageBitmap != null) {
                uploadImagine.isEnabled = true;
                descriereProblema.isEnabled = true;
            };
        }

    private fun encodeBitmapAndSaveToFirebase() {

    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setat actiune butonul takePhoto
        val takePhotoBtn = view.findViewById<View>(R.id.take_photo) as Button
        takePhotoBtn.setOnClickListener {
            val firebaseAuth = FirebaseAuth.getInstance();
            val user = firebaseAuth.currentUser;
            // verificat daca exista user autentificat
            if (user == null) {
                // daca nu este nimeni logat, afisat mesaj
                // cu 2 butoane
                // la click pe butonul de OK, declansam procesul de logare
                val view = this.view?.findViewById(R.id.home_view) as View
                MaterialAlertDialogBuilder(view.context)
                    .setTitle(resources.getString(R.string.signin_required))
                    .setMessage(resources.getString(R.string.signin_required_long))
                    .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                        // Respond to negative button press
                    }
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                        signInLauncher.launch(SigninIntentBuilder().createSignInIntent())
                    }
                    .show()
            } else {
                this.takePhoto();
            }
        }

        val uploadImagine = view.findViewById<View>(R.id.upload_photo) as Button
        uploadImagine.setOnClickListener {
            urcaProblema();
            debug("upload photo clicked")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun urcaProblema() {
        val baos = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageEncoded: String = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        val user = FirebaseAuth.getInstance().currentUser!!.uid
        val descriereProblema =
            this.view?.findViewById<View>(R.id.problem_description) as TextInputLayout
        val problemaSemnalata =
            ProblemaSemnalata(
                UUID.randomUUID(),
                user,
                imageEncoded,
                descriereProblema.editText?.text.toString()
            )
        val db = Firebase.firestore;
        db.collection("probleme_semnalate")
            .add(problemaSemnalata)
            .addOnSuccessListener { documentReference ->
                debug("DocumentSnapshot added with ID: ${documentReference.id}")
                val view = this.view?.findViewById(R.id.home_view) as View
                Snackbar.make(
                    view,
                    R.string.problema_salvata_ok,
                    Snackbar.LENGTH_SHORT
                ).show()
//                view.refreshDrawableState()
            }
            .addOnFailureListener { e ->
                warn("Error adding document", e)
            }
    }

    private fun warn(s: String, e: Exception) {
        Log.w(LOG_TAG, s, e)
    }

    fun debug(message: String) {
        Log.d(LOG_TAG, message)
    }

    fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
        debug("trigger camera")
        imageUploadLauncherResult.launch(takePictureIntent);
//        }
    }
}