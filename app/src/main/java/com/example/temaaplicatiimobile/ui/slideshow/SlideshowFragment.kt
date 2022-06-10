package com.example.temaaplicatiimobile.ui.slideshow

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.temaaplicatiimobile.R
import com.example.temaaplicatiimobile.databinding.FragmentSlideshowBinding
import com.google.android.material.button.MaterialButton

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var simpleVideoView: VideoView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var button = view.findViewById<MaterialButton>(R.id.playVideo)
        simpleVideoView = view.findViewById<View>(R.id.simpleVideoView) as VideoView
        button.setOnClickListener {

            var mediaControls = MediaController(it.context)
            var uri =
                Uri.parse("https://www.ebookfrenzy.com/android_book/movie.mp4")
            mediaControls.setAnchorView(simpleVideoView)
            simpleVideoView.setMediaController(mediaControls)

            simpleVideoView.setVideoURI(uri);
            simpleVideoView.requestFocus();
            simpleVideoView.start();


            // afisare mesaj cand videoul a terminat de rulat
            simpleVideoView.setOnCompletionListener {
                Toast.makeText(
                    context, "Video completed",
                    Toast.LENGTH_LONG
                ).show()
            }

            // afisare mesaj in caz de eroare
            simpleVideoView.setOnErrorListener { mp, what, extra ->
                Toast.makeText(
                    context, "An Error Occurred " +
                            "While Playing Video !!!", Toast.LENGTH_LONG
                ).show()
                false
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}