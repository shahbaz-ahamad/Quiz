package com.example.quiz.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentQuizBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.quiz.datamodel.Result
import com.google.android.material.snackbar.Snackbar


@AndroidEntryPoint
class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var questionData : List<Result>
    private var currentQuestion : Int =0
    private var countDownTimer: CountDownTimer? = null
    private var correctAns :Int =0
    private var wrongAnswer:Int =0
    private var missedAnswer:Int = 0
    private var selectedButton : AppCompatButton? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentQuizBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionData = arguments?.getParcelableArrayList("questionData")!!
        Log.d("data",questionData.toString())
        updateUI()
        quizTimer()
        binding.nextQueBtn.setOnClickListener {
            if(selectedButton != null) {
                if (currentQuestion < questionData.size - 1) {
                    currentQuestion++
                    updateUI()
                    quizTimer()
                    resetBackground()
                } else {
                    // Handle the end of the quiz, e.g., navigate to a result fragment.
                    goToResultfragment()
                }
            }else{
                Snackbar.make(binding.root,"Select Option",Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }
        clickListnerForButton()
    }

    private fun clickListnerForButton() {
        binding.option1Btn.setOnClickListener{onOptionButtonClicked(binding.option1Btn)}
        binding.option2Btn.setOnClickListener{onOptionButtonClicked(binding.option2Btn)}
        binding.option3Btn.setOnClickListener{onOptionButtonClicked(binding.option3Btn)}
        binding.option4Btn.setOnClickListener{onOptionButtonClicked(binding.option4Btn)}
    }

    private fun onOptionButtonClicked(button : AppCompatButton){
        //reference of the selected button
        selectedButton = button
        stopQuizTimer()
        verifyAnswer()
        disableButton()
    }

    private fun verifyAnswer() {
        val selecetedAns = selectedButton?.text.toString()
        if(selecetedAns == questionData[currentQuestion].correct_answer){
            correctAns++
            selectedButton?.setBackgroundColor(resources.getColor(R.color.correctans))
            binding.ansFeedbackTv.text="Correct ans:${questionData[currentQuestion].correct_answer}"
        }else{
            wrongAnswer++
            selectedButton?.setBackgroundColor(resources.getColor(R.color.Incorrectans))
            binding.ansFeedbackTv.text="Correct ans:${questionData[currentQuestion].correct_answer}"
        }
    }

    private fun disableButton(){
            binding.option1Btn.isEnabled=false
            binding.option2Btn.isEnabled=false
            binding.option3Btn.isEnabled=false
            binding.option4Btn.isEnabled=false
    }

    private fun enableButton(){
        binding.option1Btn.isEnabled=true
        binding.option2Btn.isEnabled=true
        binding.option3Btn.isEnabled=true
        binding.option4Btn.isEnabled=true
    }

    private fun resetBackground(){
        binding.option1Btn.setBackgroundColor(resources.getColor(R.color.light_sky))
        binding.option2Btn.setBackgroundColor(resources.getColor(R.color.light_sky))
        binding.option3Btn.setBackgroundColor(resources.getColor(R.color.light_sky))
        binding.option4Btn.setBackgroundColor(resources.getColor(R.color.light_sky))
        enableButton()
    }

    private fun updateUI() {
        val question = questionData[currentQuestion]
        binding.quizQuestionsCount.text = (currentQuestion + 1).toString()
        binding.quizQuestionTv.text = question.question

        // Combine correct and incorrect answers
        val allAnswers = mutableListOf(
            question.correct_answer,
            question.incorrect_answers[0],
            question.incorrect_answers[1],
            question.incorrect_answers[2]
        )

        // Shuffle the answers
        allAnswers.shuffle()

        // Assign the shuffled answers to the buttons
        binding.option1Btn.text = allAnswers[0]
        binding.option2Btn.text = allAnswers[1]
        binding.option3Btn.text = allAnswers[2]
        binding.option4Btn.text = allAnswers[3]

        binding.ansFeedbackTv.text = ""

        if (currentQuestion == questionData.size - 1) {
            binding.nextQueBtn.text = "Go To Result"
        }
        selectedButton= null
    }

    private fun quizTimer(){
        countDownTimer = object : CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val remSecond = (millisUntilFinished / 1000)
                binding.countTimeQuiz.text=remSecond.toString()

                //update the progress bar based on the remaining second
                // Update the progress bar based on remaining time
                val progress = ((millisUntilFinished.toFloat() / 10000) * 100).toInt()
                binding.quizCoutProgressBar.progress = progress

            }

            override fun onFinish() {
                binding.quizCoutProgressBar.visibility=View.INVISIBLE
                if(currentQuestion < questionData.size -1){
                    currentQuestion ++
                    quizTimer() // so the timer start for the each and every question
                    updateUI()
                    binding.quizCoutProgressBar.visibility=View.VISIBLE
                    missedAnswer++

                }else{
                    //navigate to the result fragment
                    missedAnswer++
                    goToResultfragment()
                }
            }

        }
        countDownTimer?.start()

    }

    private fun stopQuizTimer(){
        countDownTimer?.cancel()
    }

    private fun goToResultfragment(){

        val data = Bundle().apply {
            putInt("CorrectAns",correctAns)
            putInt("IncorrectAns",wrongAnswer)
            putInt("MissedQuestion",missedAnswer)
        }
        findNavController().navigate(R.id.action_quizFragment_to_resultFragment,data)
    }

}