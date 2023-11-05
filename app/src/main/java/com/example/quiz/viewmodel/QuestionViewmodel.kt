package com.example.quiz.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz.datamodel.QuestionCollection
import com.example.quiz.datamodel.Result
import com.example.quiz.helper.Resource
import com.example.quiz.retrofit.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuestionViewmodel @Inject constructor(
    private val apiServices: ApiServices
) : ViewModel() {

    private val _question = MutableStateFlow<Resource<List<Result>>>(Resource.Unspecified())
    val question = _question.asStateFlow()


    fun fetchQuestion(amount: Int, category: Int, difficulty: String, type: String) {
        viewModelScope.launch {
            _question.emit(Resource.Loading())
            try {

                apiServices.getQuestions(amount, category, difficulty, type)
                    .enqueue(object : Callback<QuestionCollection> {
                        override fun onResponse(
                            call: Call<QuestionCollection>,
                            response: Response<QuestionCollection>
                        ) {
                            if (response.isSuccessful) {
                                val data = response.body()?.results
                                if (data != null) {
                                    _question.value = Resource.Success(data)
                                } else {
                                    viewModelScope.launch {
                                        _question.emit(Resource.Error("Data null"))
                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<QuestionCollection>, t: Throwable) {
                            viewModelScope.launch {
                                _question.emit(Resource.Error(t.message.toString()))
                            }
                        }

                    })


            } catch (e: Exception) {
                _question.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}