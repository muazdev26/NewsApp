package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.ArticlesAdapter
import com.example.newsapp.databinding.FragmentAllHeadlinesBinding
import com.example.newsapp.networking.Article
import com.example.newsapp.ui.viewmodels.NewsViewModel
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource

class AllHeadlinesFragment : Fragment() {

    private var _binding: FragmentAllHeadlinesBinding? = null
    private val binding get() = _binding!!

    private lateinit var allHeadLinesAdapter: ArticlesAdapter
    private lateinit var viewModel: NewsViewModel

    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllHeadlinesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }
        setUpObserver()
    }

    private fun setUpObserver() {
        binding.apply {
            viewModel.allHeadLinesLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        pbLoading.hide()
                        it.responseBody?.articles?.toList()?.let { it1 -> setUpRV(it1) }

                        val totalPages =
                            (it.responseBody?.totalResults?.div(Constants.QUERY_PAGE_SIZE))?.and(2)
                        isLastPage = viewModel.breakingNewsPage == totalPages
                    }
                    is Resource.Failure -> {
                        pbLoading.hide()
                        Log.d(Constants.TAG, "setUpObserver: ${it.message}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        pbLoading.show()
                    }
                }
            }
        }
    }

    private fun setUpRV(newslist: List<Article>) {
        allHeadLinesAdapter = ArticlesAdapter(newslist) { _, _, i, _ ->
            val action =
                AllHeadlinesFragmentDirections.actionAllHeadlinesFragmentToNewsDetailsFragment(
                    newslist[i]
                )
            action.news = newslist[i]
            findNavController().navigate(action)
        }
        binding.rvAlHeadLines.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = allHeadLinesAdapter
            addOnScrollListener(this@AllHeadlinesFragment.scrollListener)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = binding.rvAlHeadLines.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val allVisibleItemCount = layoutManager.childCount

            val totalItemCountInRV = layoutManager.itemCount

            val isNotLoadingNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + allVisibleItemCount >= totalItemCountInRV

            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCountInRV >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate =
                isNotLoadingNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible
                        && isScrolling

            if (shouldPaginate) {
                viewModel.getAllHeadLines("us")
                isScrolling = false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}