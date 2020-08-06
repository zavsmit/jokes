package com.zavsmit.jokes.ui.jokes_list

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zavsmit.jokes.R
import kotlinx.android.synthetic.main.item_joke.view.*

class JokesAdapter() : RecyclerView.Adapter<JokesAdapter.MyViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var jokes = listOf<UiModelJoke>()

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
        val joke = jokes[position]
        holder.tvItem.text = joke.text

        holder.bLike.apply {
            text = joke.likeButtonText
            setOnClickListener { onClickListener() }
        }

        holder.bShare.apply {
            visibility = if (joke.isVisibleShare) VISIBLE else GONE
            setOnClickListener { onClickListener() }
        }
    }

    override fun getItemCount() = jokes.size

    fun setData(data: List<UiModelJoke>) {
        jokes = data
        notifyDataSetChanged()
    }

    fun changeItem(data: List<UiModelJoke>, position: Int) {
        jokes = data
        notifyItemChanged(position)
    }


    fun onClickListener() {}
}