package com.example.newsapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticlesAdapter
import com.example.newsapp.database.ArticlesDatabase
import com.example.newsapp.databinding.FragmentCategoryNewsBinding
import com.example.newsapp.networking.Article
import com.example.newsapp.repository.AppRepository
import com.example.newsapp.ui.viewmodels.SearchNewsViewModel
import com.example.newsapp.ui.viewmodels.SearchNewsViewModelFactory
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Resource

class CategoryNewsFragment : Fragment() {

    private var _binding: FragmentCategoryNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchNewsViewModel
    private lateinit var args: CategoryNewsFragmentArgs

    private lateinit var articlesAdapter: ArticlesAdapter

//    var isLoading = false
//    var isLastPage = false
//    var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUtils()
        setUpObserver()
    }

    private fun setUpRV(newsList: List<Article>) {
        articlesAdapter = ArticlesAdapter(newsList) { _, _, i, _ ->
            val action =
                AllHeadlinesFragmentDirections.actionAllHeadlinesFragmentToNewsDetailsFragment(
                    newsList[i]
                )
            action.news = newsList[i]
            findNavController().navigate(action)
        }
        binding.rvAlHeadLines.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = articlesAdapter
//            addOnScrollListener(this@CategoryNewsFragment.scrollListener)
        }
    }

    private fun setUpObserver() {
        binding.apply {
            searchViewModel.searchNews(args.query)
            searchViewModel.searchNewsLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        pbLoading.hide()
                        it.responseBody?.articles?.let { it1 -> setUpRV(it1) }
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

    @SuppressLint("SetTextI18n")
    private fun initUtils() {
        args = CategoryNewsFragmentArgs.fromBundle(requireArguments())
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
            tvCategoryName.text =
                "${requireContext().getString(R.string.what_s_happening_in)} ${args.query}"
        }
        val appRepository = AppRepository(ArticlesDatabase.invoke(requireContext()))
        val searchNewsViewModelFactory = SearchNewsViewModelFactory(appRepository)
        searchViewModel =
            ViewModelProvider(
                this,
                searchNewsViewModelFactory
            ).get(SearchNewsViewModel::class.java)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = binding.rvAlHeadLines.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val allVisibleItemCount = layoutManager.childCount

//            val totalItemCountInRV = layoutManager.itemCount
//
//            val isNotLoadingNotLastPage = !isLoading && !isLastPage
//            val isAtLastItem = firstVisibleItemPosition + allVisibleItemCount >= totalItemCountInRV
//
//            val isNotAtBeginning = firstVisibleItemPosition >= 0
//            val isTotalMoreThanVisible = totalItemCountInRV >= Constants.QUERY_PAGE_SIZE
//
//            val shouldPaginate =
//                isNotLoadingNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible
//                        && isScrolling
//
//            if (shouldPaginate) {
//                searchViewModel.searchNews(args.query)
//                isScrolling = false
//            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}