package com.example.pruebavalid.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pruebavalid.R
import com.example.pruebavalid.databinding.ActivityArtistBinding
import com.example.pruebavalid.util.toast
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paginate.Paginate
import com.paginate.recycler.LoadingListItemCreator
import android.widget.TextView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pruebavalid.data.db.entities.ResponseTopArtists
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_artist.*
import android.os.Handler
import android.widget.SearchView
import retrofit2.Retrofit


class ArtistActivity : AppCompatActivity(), AuthListener, Paginate.Callbacks,
    SearchView.OnQueryTextListener {

    private var paginate: Paginate? = null

    private var adapter: ArtistsAdapter? = null

    private var page = 0

    private var loading = false

    private val handler = Handler()

    private val fakeCallback = Runnable {
        page++
        loading = false
    }

    override fun onStarted() {

    }

    override fun onSuccess(artistResponse: LiveData<String>) {
        artistResponse.observe(this, Observer {

            val topArtists = Gson().fromJson<ResponseTopArtists>(it, ResponseTopArtists::class.java)

            adapter =
                ArtistsAdapter(this, topArtists.topartists.artist, topArtists.topartists.artist)
            rvArtist.adapter = adapter

            paginate = Paginate.with(rvArtist, this)
                .setLoadingTriggerThreshold(1)
                .setLoadingListItemCreator(CustomLoadingListItemCreator())
                .build()
        })
    }

    override fun onFailure(message: String) {
        toast("Information did not get it")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityArtistBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_artist)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this
        viewModel.getArtist()

        ivSearchArtist.setOnQueryTextListener(this)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter?.filter?.filter(newText)
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter?.filter?.filter(query)
        return false
    }

    override fun onLoadMore() {
        loading = true
        handler.postDelayed(fakeCallback, 200)
    }


    override fun isLoading() = loading

    override fun hasLoadedAllItems() = page == 1

    inner class CustomLoadingListItemCreator : LoadingListItemCreator {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.custom_loading_list_item, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val vh = holder as VH
            vh.tvLoading.text = String.format(
                "Total items loaded: %d.\nLoading more...",
                adapter?.itemCount
            )
            if (rvArtist.layoutManager is StaggeredGridLayoutManager) {
                val params =
                    vh.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                params.isFullSpan = true
            }
        }
    }

    internal class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLoading: TextView = itemView.findViewById(R.id.tv_loading_text)
    }
}
