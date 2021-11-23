package com.example.newsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsapp.adapters.CategoriesAdapter
import com.example.newsapp.databinding.FragmentAllCategoriesBinding
import com.example.newsapp.utils.CategoryUtils

class AllCategoriesFragment : Fragment() {

    private var _binding: FragmentAllCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoriesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setUpRV() {
        categoryAdapter = CategoriesAdapter { _, _, i, _ ->
            val action =
                AllCategoriesFragmentDirections.actionAllCategoriesFragmentToCategoryNewsFragment("")
            action.query = categoryAdapter.currentList[i].categoryName
            findNavController().navigate(action)
        }
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
        }
        categoryAdapter.submitList(CategoryUtils.getCategories())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}