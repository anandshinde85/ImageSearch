package com.axxess.imagesearch.imagesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axxess.imagesearch.R
import com.axxess.imagesearch.common.util.loadImage
import com.axxess.imagesearch.networking.imagesearch.SearchResults
import kotlinx.android.synthetic.main.search_result_item.view.*

class ImageSearchAdapter(
    private val searchResultsList: MutableList<SearchResults> = arrayListOf(),
    private val onSearchItemClick: (seachResults: SearchResults) -> Unit = {}
) :
    RecyclerView.Adapter<ImageSearchAdapter.SearchResultHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultHolder =
        SearchResultHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_result_item, parent, false
            )
        )

    override fun getItemCount() = searchResultsList.size

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        val searchResult = searchResultsList[position]
        with(holder) {
            imageTitle.text = searchResult.title
            val image = searchResult.images?.firstOrNull()
            image?.let {
                searchImage.loadImage(it.link)
                it.views?.let { this.viewsLabel.text = it } ?: run { setNoViewsFound(viewsLabel) }
            } ?: run { setNoViewsFound(viewsLabel) }

            itemView.setOnClickListener { onSearchItemClick(searchResult) }
        }
    }

    inner class SearchResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTitle: TextView = itemView.imageTitle
        val searchImage: ImageView = itemView.searchImage
        val viewsLabel: TextView = itemView.viewsLabel
    }

    fun updateDataSet(searchList: List<SearchResults>) = searchResultsList.run {
        clear()
        addAll(searchList)
        notifyDataSetChanged()
    }

    private fun setNoViewsFound(textView: TextView) =
        textView.run { text = textView.context.getString(R.string.no_views_available) }
}