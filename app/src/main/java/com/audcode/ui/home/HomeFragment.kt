package com.audcode.ui.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.audcode.R
import com.audcode.ui.BaseFragment
import com.audcode.ui.HomeAdapter
import com.audcode.ui.OnClickListener
import com.audcode.ui.home.model.EpisodeModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() , OnClickListener {
    override fun getLayoutById() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<EpisodeModel>()

       for (i in 1..8){
           val item = EpisodeModel("Ask Developer Podcast","","Mohamded Alsharief",5.0f,70,"Backend")
           list.add(item)
       }

        val homeAdapter = HomeAdapter()
        val linearLayoutManager = LinearLayoutManager(activity)
        episodeList.apply {
            layoutManager = linearLayoutManager
            adapter = homeAdapter
        }
        homeAdapter.setClickListener(this)

        homeAdapter.addGifs(list)
    }

    override fun onClick(position: Int, view: View) {
    }

}