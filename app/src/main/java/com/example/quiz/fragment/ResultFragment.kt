package com.example.quiz.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentResultBinding
import com.example.quiz.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var correctAns :Int =0
    private var wrongAnswer:Int =0
    private var missedAnswer:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentResultBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getResult()
        updateUI()
        calculateResult()
        binding.homeBtn.setOnClickListener{
            findNavController().navigate(R.id.action_resultFragment_to_listFragment)
        }

    }

    private fun updateUI() {
        binding.correctAnswerTv.text=correctAns.toString()
        binding.wrongAnswersTv.text=wrongAnswer.toString()
        binding.notAnsweredTv.text=missedAnswer.toString()
    }

    private fun getResult() {
        val args = arguments
        correctAns= args?.getInt("CorrectAns",0)!!
        wrongAnswer= args?.getInt("IncorrectAns",0)!!
        missedAnswer= args?.getInt("MissedQuestion",0)!!
    }

    private fun calculateResult(){
        val result = (correctAns.toFloat()/(wrongAnswer + missedAnswer).toFloat())*100
        binding.resultPercentageTv.text="${result}%"
        binding.resultCoutProgressBar.progress = result.toInt()
    }



}