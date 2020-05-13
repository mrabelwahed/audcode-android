package com.audcode.ui.library

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.audcode.R
import com.audcode.ui.*
import com.audcode.ui.decoration.SpacesItemDecoration
import com.audcode.ui.episode_details.EpisodeDetailsFragment
import com.audcode.ui.home.model.EpisodeModel
import com.audcode.ui.splash.MainActivity
import kotlinx.android.synthetic.main.fragment_saved_episodes.*
import javax.inject.Inject

class LibraryFragment : BaseFragment() , OnClickListener {
    private lateinit var episodesLayoutManager:LinearLayoutManager
    @Inject lateinit var  libraryAdapter :LibraryAdapter
    private lateinit var libraryVM: LibraryVM
    override fun getLayoutById() = R.layout.fragment_saved_episodes
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBar.visibility = View.VISIBLE
        toolBar.title = getString(R.string.menu_library)
        holderActivity.setSupportActionBar(toolBar)
        // add back arrow to toolbar
        if (holderActivity.supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
        toolBar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_ios_24px)
        toolBar.setNavigationOnClickListener(View.OnClickListener {
            // back button pressed
            holderActivity.onBackPressed()
        })

        setupLibraryAdapterWithData(loadSavedEpisodes())
    }

    private fun setupLibraryAdapterWithData(episodes :ArrayList<EpisodeModel>) {
            episodesLayoutManager = LinearLayoutManager(context)
            libraryAdapter.setClickListener(this)
            savedEpisodesRV.apply {
                layoutManager = episodesLayoutManager
                addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelOffset(R.dimen.spacing_small)))
                adapter = libraryAdapter
                if(episodes.isNotEmpty()){
                    libraryAdapter.addEpisodes(episodes)
                    visibility = View.VISIBLE
                    emptyText.visibility = View.GONE
                }
            }
    }

    override fun onClick(position: Int, view: View) {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.SELECTED_EPISODE, libraryAdapter.episodes[position])
        val episodeDetailsFragment = EpisodeDetailsFragment()
        episodeDetailsFragment.arguments = bundle
        (activity as MainActivity).supportFragmentManager.beginTransaction().replace(
            R.id.container,
            episodeDetailsFragment
        ).addToBackStack("episode_details_transaction").commit()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        app.appComponent.newLibraryComponent().inject(this)
        libraryVM = ViewModelProvider(this, viewModelFactory)[LibraryVM::class.java]
        bottomNavigation.visibility = View.VISIBLE
    }

}