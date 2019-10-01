package com.erikriosetiawan.cinemamovies.ui.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erikriosetiawan.cinemamovies.R
import com.erikriosetiawan.cinemamovies.adapter.TvShowAdapter
import com.erikriosetiawan.cinemamovies.model.TvShow

class TvShowFragment : Fragment() {

    private lateinit var tvShowViewModel: TvShowViewModel
    private var tvShows: MutableList<TvShow> = mutableListOf()
    private lateinit var root: View
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tvShowViewModel =
            ViewModelProviders.of(this).get(TvShowViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_tv_show, container, false)
        initView()
        tvShowViewModel.open()
        tvShowViewModel.getTvShows().observe(this, Observer {
            tvShows.addAll(it)
            tvShowViewModel.close()
        })

        tvShowViewModel.getIsFetching().observe(this, Observer {
            if (it) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
        })
        setRecyclerList(root)
        return root
    }

    private fun setRecyclerList(root: View) {
        val rvTvShow: RecyclerView = root.findViewById(R.id.rv_tv_show)
        rvTvShow.layoutManager = LinearLayoutManager(root.context)
        rvTvShow.adapter = TvShowAdapter(root.context, tvShows)
    }

    private fun initView() {
        progressBar = root.findViewById(R.id.progress_bar)
    }
}