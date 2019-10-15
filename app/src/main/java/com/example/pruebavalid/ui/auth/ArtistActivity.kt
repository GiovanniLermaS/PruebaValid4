package com.example.pruebavalid.ui.auth

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AbsListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebavalid.R
import com.example.pruebavalid.data.db.entities.Artist
import com.example.pruebavalid.data.db.entities.ResponseTopArtists
import com.example.pruebavalid.databinding.ActivityArtistBinding
import com.example.pruebavalid.util.toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_artist.*


class ArtistActivity : AppCompatActivity(), AuthListener,
    SearchView.OnQueryTextListener {

    private var adapter: ArtistsAdapter? = null

    override fun onStarted() {

    }

    private var artists = ArrayList<Artist>()

    private var isScrolling = false

    private var currentItems = 0

    private var totalItems = 0

    private var scrollOutItems = 0

    private var topArtists: ResponseTopArtists? = null

    override fun onSuccess(artistResponse: LiveData<String>) {
        artistResponse.observe(this, Observer {

            topArtists = Gson().fromJson<ResponseTopArtists>(it, ResponseTopArtists::class.java)

            for (i in 0..6)
                artists.add(topArtists?.topartists?.artist!![i])

            adapter =
                ArtistsAdapter(this, artists, artists)
            rvArtist.adapter = adapter

            rvArtist.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        isScrolling = true
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val manager = (rvArtist.layoutManager) as LinearLayoutManager
                    currentItems = manager.childCount
                    totalItems = manager.itemCount
                    scrollOutItems = manager.findFirstVisibleItemPosition()
                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        isScrolling = false
                        fetchData()
                    }
                }
            })
        })
    }

    private fun fetchData() {
        progress.visibility = View.VISIBLE
        Handler().postDelayed({
            for (i in 0..6)
                artists.add(topArtists?.topartists?.artist!![i])
            adapter?.notifyDataSetChanged()
            progress.visibility = View.GONE
        }, 2000)
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
}
