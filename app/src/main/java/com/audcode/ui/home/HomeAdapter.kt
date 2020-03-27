package com.audcode.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.audcode.R
import com.audcode.ui.home.model.EpisodeModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_episode.view.*
import javax.inject.Inject


class HomeAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val episodes = ArrayList<EpisodeModel>()
    lateinit var listener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent,false)
        return EpisodeViewHolder(view)
    }

    override fun getItemCount() = episodes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EpisodeViewHolder) {

            holder.author.text = episodes[position].author
            holder.title.text = episodes[position].name
          // holder.stack.text = episodes[position].mainStack
           // holder.ratingCount.text = episodes[position].ratingCount.toString()
            Glide.with(holder.itemView.context).load(if (position%2 ==0) (R.drawable.me) else (R.drawable.kot)).into(holder.image)
            holder.itemView.setOnClickListener {
                listener.onClick(position, it)
            }
        }

    }


    //main item
    class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.episodeAuthor
        val title: TextView = view.episodeTitle
        //val stack: TextView = view.episodeMainStack
        val image:ImageView = view.episodeImage
        //val ratingCount:TextView = view.ratingCount
    }


    fun addEpisodes(list: ArrayList<EpisodeModel>) {
        episodes.addAll(list)
        notifyDataSetChanged()
    }

    fun clearAll() {
        episodes.clear()
        notifyDataSetChanged()
    }

    fun setClickListener(listener: OnClickListener) {
        this.listener = listener
    }

}


