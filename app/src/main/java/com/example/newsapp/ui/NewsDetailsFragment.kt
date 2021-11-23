package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsDetailsBinding
import com.example.newsapp.networking.Article
import com.example.newsapp.ui.viewmodels.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsDetailsFragment : Fragment() {

    private var _binding: FragmentNewsDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }

        setNews()
    }


    private fun setNews() {
        val news = NewsDetailsFragmentArgs.fromBundle(requireArguments()).news
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(news.url)
        }
        setSavedNews(news)
        binding.fabSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (viewModel.isNewsExists(news.url)) {
                    viewModel.deleteNews(news)
                    withContext(Dispatchers.Main) {
                        binding.fabSave.setImageResource(R.drawable.ic_unfavorite)
                        Toast.makeText(
                            requireContext(),
                            "News removed successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    viewModel.saveNews(news)
                    withContext(Dispatchers.Main) {
                        binding.fabSave.setImageResource(R.drawable.ic_favorite)
                        Toast.makeText(
                            requireContext(),
                            "News saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setSavedNews(news: Article) {
        CoroutineScope(Dispatchers.IO).launch {
            if (viewModel.isNewsExists(news.url)) {
                withContext(Dispatchers.Main) {
                    binding.fabSave.setImageResource(R.drawable.ic_favorite)
                }
            } else {
                withContext(Dispatchers.Main) {
                    binding.fabSave.setImageResource(R.drawable.ic_unfavorite)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}