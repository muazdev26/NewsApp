package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.ArticlesAdapter
import com.example.newsapp.databinding.FragmentBookMarksBinding
import com.example.newsapp.networking.Article
import com.example.newsapp.ui.viewmodels.NewsViewModel

class BookMarksFragment : Fragment() {

    private var _binding: FragmentBookMarksBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewsViewModel
    private lateinit var articlesAdapter: ArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookMarksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setUpObserver()
    }

    private fun setUpRV(newslist : List<Article>) {
        articlesAdapter = ArticlesAdapter(newslist) { _, _, i, _ ->
            val action = BookMarksFragmentDirections.actionNavBookmarksToNewsDetailsFragment(
                newslist[i]
            )
            action.news = newslist[i]
            findNavController().navigate(action)
        }
        binding.rvBookmarks.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun setUpObserver() {
        binding.apply {
            viewModel.getSavedNews().observe(viewLifecycleOwner) {
               setUpRV(it)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}