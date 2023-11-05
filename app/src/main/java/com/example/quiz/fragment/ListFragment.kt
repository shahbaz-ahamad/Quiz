package com.example.quiz.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quiz.R
import com.example.quiz.adapter.CategoryAdapter
import com.example.quiz.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var categoryList:ArrayList<String>
    private lateinit var categoryImage: ArrayList<Int>
    private val listAdapter by lazy {
        CategoryAdapter(categoryList,categoryImage)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCategoryItem()
        addCategoryImage()
        setupRecyclerView()
        listAdapter.onClick = {category,image ->
            val data = Bundle().apply {
                putString("category",category)
                putInt("image",image)
            }
            findNavController().navigate(R.id.action_listFragment_to_detailsFragment,data)
        }
    }

    private fun addCategoryItem() {
        categoryList = ArrayList()
        categoryList.add("Vehicles")
        categoryList.add("Animals")
        categoryList.add("Computer")
        categoryList.add("Politics")
        categoryList.add("Sports")
    }

    private fun addCategoryImage(){
        categoryImage= ArrayList()
        categoryImage.add(R.drawable.vechiles)
        categoryImage.add(R.drawable.animals)
        categoryImage.add(R.drawable.splash_image)
        categoryImage.add(R.drawable.politics)
        categoryImage.add(R.drawable.sport)
    }

    private fun setupRecyclerView() {
        binding.recyclerview.apply {

            layoutManager= GridLayoutManager(requireContext(),3)
            adapter=listAdapter
        }
    }



}