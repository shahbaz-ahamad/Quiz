package com.example.quiz.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.quiz.R
import com.example.quiz.databinding.FragmentDetailsBinding
import com.example.quiz.helper.Resource
import com.example.quiz.viewmodel.QuestionViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.quiz.datamodel.Result
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var category:String
    private val viewmodel by viewModels<QuestionViewmodel>()
    private lateinit var data :List<Result>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data= listOf()
        observeQuestion()
        getDataFromListFragment()




        //check which category to be called
        checkCategory()


        binding.startQuizBtn.setOnClickListener {
            val data = Bundle().apply {
                putParcelableArrayList("questionData", ArrayList(data))
            }

            findNavController().navigate(R.id.action_detailsFragment_to_quizFragment,data)

        }

    }

    private fun getDataFromListFragment() {
        category= requireArguments().getString("category").toString()
        val image = requireArguments().getInt("image")
        binding.detailFragmentImage.setImageResource(image)
    }


    private fun checkCategory() {
        if(category == "Vehicles"){
            viewmodel.fetchQuestion(10,28,"medium","multiple")
        }else if(category == "Animals"){
            viewmodel.fetchQuestion(10,27,"easy","multiple")
        }else if(category ==  "Computer" ){
            viewmodel.fetchQuestion(18,25,"medium","multiple")
        }else if(category == "Politics"){
            viewmodel.fetchQuestion(10,24,"hard","multiple")
        }else if(category == "Sports"){
            viewmodel.fetchQuestion(10,21,"easy","multiple")
        }
    }

    private fun observeQuestion(){
        lifecycleScope.launch {
            viewmodel.question.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.detailProgressBar.visibility=View.VISIBLE
                        binding.startQuizBtn.isEnabled=false

                    }
                    is Resource.Success ->{
                        binding.detailProgressBar.visibility=View.INVISIBLE
                        binding.startQuizBtn.isEnabled=true
                        Log.d("data", it.data.toString())
                        updateUi(it.data)
                        if(it.data!= null) {
                            data = it.data!!
                        }
                    }

                    is Resource.Error ->{
                        binding.detailProgressBar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("Error",it.message.toString())
                    }else -> Unit
                }
            }
        }
    }

    private fun updateUi(data: List<Result>?) {
        if (data.isNullOrEmpty()) {
            // Handle the case when the list is empty or null
            // You might want to display an error message or take appropriate action
            showEmptyListSnackbar()
            binding.startQuizBtn.isEnabled=false
        } else {
            binding.apply {
                detailFragmentTitle.text = data[0].category
                detailFragmentDifficulty.text = data[0].difficulty
                detailFragmentQuestions.text = data.size.toString()
            }
        }
    }

    private fun showEmptyListSnackbar() {
        Snackbar.make(binding.root, "The list is empty.", Snackbar.LENGTH_SHORT).show()
    }
}