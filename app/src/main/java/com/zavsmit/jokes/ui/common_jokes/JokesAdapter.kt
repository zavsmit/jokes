package com.zavsmit.jokes.ui.common_jokes

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zavsmit.jokes.R
import com.zavsmit.jokes.domain.models.UiModelJoke
import kotlinx.android.synthetic.main.item_joke.view.*

class JokesAdapter(private val onLikeClicked: (Long) -> Unit,
                   private val onShareClicked: (String) -> Unit,
                   private val isMyJokes: Boolean = false) : ListAdapter<UiModelJoke, JokesAdapter.MyViewHolder>(DiffCallback()) {
    private var layoutInflater: LayoutInflater? = null

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvItem: TextView = v.tv_item
        var bLike: Button = v.b_like
        var bShare: Button = v.b_share
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = getLayoutInflater(parent).inflate(R.layout.item_joke, parent, false)
        return MyViewHolder(view)
    }

    private fun getLayoutInflater(parent: ViewGroup): LayoutInflater {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        return layoutInflater!!
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val joke = getItem(position)
        holder.tvItem.text = "#${joke.id}\n${joke.text}"

        holder.bLike.apply {
            text = joke.likeButtonText
            setOnClickListener { onLikeClicked.invoke(joke.id) }
        }

        holder.bShare.apply {
            visibility = if (isMyJokes) GONE else VISIBLE
            setOnClickListener { onShareClicked.invoke(joke.text) }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<UiModelJoke>() {
    override fun areItemsTheSame(oldItem: UiModelJoke, newItem: UiModelJoke): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UiModelJoke, newItem: UiModelJoke): Boolean {
        return oldItem.id == newItem.id && oldItem.likeButtonText == newItem.likeButtonText && oldItem.text == newItem.text
    }
}
