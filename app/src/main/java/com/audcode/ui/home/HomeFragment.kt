package com.audcode.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.audcode.R
import com.audcode.data.exceptions.Failure
import com.audcode.ui.*
import com.audcode.ui.decoration.SpacesItemDecoration
import com.audcode.ui.episode_details.EpisodeDetailsFragment
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import com.audcode.ui.splash.MainActivity.Companion.SELECTED_EPISODE
import com.audcode.ui.viewmodel.ViewModelFactory
import com.audcode.ui.viewstate.ServerDataState
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.content_empty.*
import kotlinx.android.synthetic.main.content_error.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : BaseFragment(), OnClickListener {
    override fun getLayoutById() = R.layout.fragment_home
    private var totalItemCount = 0
    private var lastVisibleItem = 0
    private var loading = false
    private lateinit var episodesLayoutManager: LinearLayoutManager
    private val VISIBLE_THRESHOLD = 1
    private var newQueryIsFired = false


    lateinit var homeAdapter: HomeAdapter


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        RxTextView.textChanges(searchInput)
            .filter { text -> text.length >= 3 || text.isEmpty() }
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isEmpty()) {
                    homeVM.setNewQuery(null)
                    homeVM.loadNextPage()
                } else {
                    homeVM.setNewQuery(it.toString())
                    homeVM.loadNextPage()
                }

                homeAdapter.clearAll()
                shimmerView.visibility = View.VISIBLE
                shimmerView.startShimmer()
            }


    }

    private fun initUI() {
        homeAdapter = HomeAdapter()
        setupView()
        setupLoadMoreListener()
        observeEpisodes()
        observeLastPlayingEpisode()
    }






    private fun setupView() {
        episodesLayoutManager = LinearLayoutManager(context)
        homeAdapter.setClickListener(this)
        episodeList.apply {
            layoutManager = episodesLayoutManager
            addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelOffset(R.dimen.spacing_small)))
            adapter = homeAdapter
        }
    }

    private fun setData(episodes: ArrayList<EpisodeModel>) {
        homeAdapter.addEpisodes(episodes)
    }

    private fun observeEpisodes() {
        homeVM.episodeLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ServerDataState.Success<*> -> {
                    val episodes = (it.item as ArrayList<EpisodeModel>)
                    handleUISuccess()
                    if (episodes.size == 0 && homeAdapter.episodes.size == 0)
                        emptyView.visibility = View.VISIBLE
                    else {
                        setData(episodes)
                    }
                }

                is ServerDataState.Error -> {
                    handleUIError()
                    setError(it.failure)
                }

                is ServerDataState.Loading -> {
                    handleUILoading()
                }
            }

        })
    }


    private fun handleUILoading() {
        loading = true
        shimmerView.visibility = View.VISIBLE
        shimmerView.startShimmer()
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
    }


    private fun handleUIError() {
        loading = false
        newQueryIsFired = false
        episodeList.visibility = View.GONE
        shimmerView.visibility = View.GONE
        shimmerView.stopShimmer()
        errorView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
    }


    private fun handleUISuccess() {
        loading = false
        newQueryIsFired = false
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
        episodeList.visibility = View.VISIBLE
        shimmerView.visibility = View.GONE
        shimmerView.stopShimmer()

    }


    private fun setError(failure: Failure?) {
        failure?.let {
            when (it) {
                is Failure.NetworkConnection -> {
                    errorText.text = getString(R.string.failure_network_connection)
                }
                is Failure.ServerError -> {
                    errorText.text = getString(R.string.failure_server_error)
                    errorImage.setImageResource(R.drawable.undraw_page_not_found_su7k)

                }
                is Failure.UnExpectedError -> {
                    errorText.text = getString(R.string.failure_unexpected_error)
                    errorImage.setImageResource(R.drawable.undraw_page_not_found_su7k)
                }
            }
        }
    }


    private fun setupLoadMoreListener() {
        episodeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = episodesLayoutManager.itemCount
                lastVisibleItem = episodesLayoutManager.findLastVisibleItemPosition()
                if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                    if (!newQueryIsFired) {
                        homeVM.incrementOffset()
                        homeVM.loadNextPage()
                    }

                    loading = true
                }
            }
        })

    }



    override fun onClick(position: Int, view: View) {
        val bundle = Bundle()
        bundle.putParcelable(SELECTED_EPISODE, homeAdapter.episodes[position])
        val episodeDetailsFragment = EpisodeDetailsFragment()
        episodeDetailsFragment.arguments = bundle
        (activity as MainActivity).supportFragmentManager.beginTransaction().replace(
            R.id.mainNavHostFragment,
            episodeDetailsFragment
        ).addToBackStack("null").commit()

    }


}