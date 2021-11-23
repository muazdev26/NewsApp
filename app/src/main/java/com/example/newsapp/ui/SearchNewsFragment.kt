package com.example.newsapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.ArticlesAdapter
import com.example.newsapp.database.ArticlesDatabase
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.networking.Article
import com.example.newsapp.repository.AppRepository
import com.example.newsapp.ui.viewmodels.SearchNewsViewModel
import com.example.newsapp.ui.viewmodels.SearchNewsViewModelFactory
import com.example.newsapp.utils.Constants.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.utils.Constants.TAG
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchNewsViewModel
    private lateinit var articlesAdapter: ArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUtils()
        addSearchListener()
        setUpObserver()
    }

    private fun initUtils() {
        val appRepository = AppRepository(ArticlesDatabase.invoke(requireContext()))
        val searchNewsViewModelFactory = SearchNewsViewModelFactory(appRepository)
        viewModel =
            ViewModelProvider(
                this,
                searchNewsViewModelFactory
            ).get(SearchNewsViewModel::class.java)
    }

    private fun addSearchListener() {
        var job: Job? = null
        binding.etSearch.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(SEARCH_NEWS_TIME_DELAY)
                        if (p0.toString().isNotEmpty()) {
                            viewModel.searchNews(p0.toString())
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }
    }

    private fun setUpObserver() {
        binding.apply {
            viewModel.searchNewsLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        pbLoading.hide()
                        it.responseBody?.articles?.let { it1 -> setUpRV(it1) }
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

    private fun setUpRV(newsList: List<Article>) {
        articlesAdapter = ArticlesAdapter(newsList) { _, _, i, _ ->
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToNewsDetailsFragment(
                newsList[i]
            )
            action.news = newsList[i]
            findNavController().navigate(action)
        }
        binding.rvSearchNews.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}