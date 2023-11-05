package com.example.quiz.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding : FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        animateZoomOut()

    }


    private fun animateZoomOut() {
        binding.splashImageView.animate()
            .scaleX(0.4f)
            .scaleY(0.4f)
            .setDuration(2000)
            .withEndAction {
                animateZoomIn()
            }
            .start()
    }

    private fun animateZoomIn() {
        binding.splashImageView.animate()
            .scaleX(30.0f)
            .scaleY(30.0f)
            .setDuration(1000)
            .withEndAction {
                // Start the new activity here
                findNavController().navigate(R.id.action_splashFragment_to_listFragment)


            }
            .start()
    }
}