package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.CategoriesAdapter
import com.example.newsapp.adapters.HeadlinesAdapter
import com.example.newsapp.databinding.FragmentNewsFeedBinding
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.CategoryUtils.getCategories
import com.example.newsapp.utils.Constants.TAG
import com.example.newsapp.utils.Constants.getGreetingMessage
import com.example.newsapp.utils.Resource

class NewsFeedFragment : Fragment() {

    private var _binding: FragmentNewsFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel
    private lateinit var headLinesAdapter: HeadlinesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsFeedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setListeners()
        setUpRV()
        setGreetingMessage()
        setUpObserver()
    }

    private fun setListeners() {
        binding.apply {
            with(findNavController()) {
                ivSearch.setOnClickListener {
                    navigate(R.id.action_nav_news_feed_to_searchNewsFragment)
                }
                tvSeeMore.setOnClickListener {
                    navigate(R.id.action_nav_news_feed_to_allHeadlinesFragment)
                }
                tvSeeMoreCategories.setOnClickListener {
                    navigate(R.id.action_nav_news_feed_to_allCategoriesFragment)
                }
            }
        }
    }

    private fun setGreetingMessage() {
        binding.tvGreetingMessage.text = getGreetingMessage()
    }

    private fun setUpObserver() {
        binding.apply {
            viewModel.allHeadLinesLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        pbLoading.hide()
                        headLinesAdapter.submitList(
                            it.responseBody?.articles?.toList()?.subList(0, 11)
                        )
                    }
                    is Resource.Failure -> {
                        pbLoading.hide()
                        Log.d(TAG, "setUpObserver: ${it.message}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        pbLoading.show()
                    }
                }
            }
        }
    }

    private fun setUpRV() {
        headLinesAdapter = HeadlinesAdapter { _, _, i, _ ->
            val action = NewsFeedFragmentDirections.actionNavNewsFeedToNewsDetailsFragment(
                headLinesAdapter.currentList[i]
            )
            action.news = headLinesAdapter.currentList[i]
            findNavController().navigate(action)
        }
        binding.rvBreakingNews.apply {
            adapter = headLinesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        val categoryAdapter = CategoriesAdapter { _, _, i, _ ->
            val action = NewsFeedFragmentDirections.actionNavNewsFeedToCategoryNewsFragment("")
            action.query = getCategories()[i].categoryName
            findNavController().navigate(action)
        }
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
        }
        categoryAdapter.submitList(getCategories().subList(0, 3))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}