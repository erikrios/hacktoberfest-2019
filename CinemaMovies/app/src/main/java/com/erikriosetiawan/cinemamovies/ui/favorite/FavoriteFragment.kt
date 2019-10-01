package com.erikriosetiawan.cinemamovies.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.astuetz.PagerSlidingTabStrip
import com.erikriosetiawan.cinemamovies.R
import com.erikriosetiawan.cinemamovies.adapter.MyPagerAdapter
import com.erikriosetiawan.cinemamovies.ui.FavoriteMovieFragment
import com.erikriosetiawan.cinemamovies.ui.FavoriteTvShowFragment


class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteViewModel =
            ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        val root = inflater.inflate(
            R.layout.fragment_favorite,
            container,
            false
        )
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        favoriteViewModel.text.observe(this, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = activity!!.findViewById(R.id.pager)
        val pagerAdapter = MyPagerAdapter(activity!!.supportFragmentManager)
        pagerAdapter.addFragment(FavoriteMovieFragment(), "Favorite Movie")
        pagerAdapter.addFragment(FavoriteTvShowFragment(), "Favorite Tv Show")
        viewPager.adapter = pagerAdapter

        val tabs: PagerSlidingTabStrip = activity!!.findViewById(R.id.tabs)
        tabs.shouldExpand = true
        tabs.indicatorColor = resources.getColor(R.color.colorPrimaryDark)
        tabs.setViewPager(viewPager)
    }


}