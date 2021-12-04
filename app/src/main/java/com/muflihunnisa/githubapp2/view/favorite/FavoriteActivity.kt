package com.muflihunnisa.githubapp2.view.favorite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.muflihunnisa.githubapp2.R
import com.muflihunnisa.githubapp2.databinding.ActivityFavoriteBinding
import com.muflihunnisa.githubapp2.domain.data.model.ItemsItem
import com.muflihunnisa.githubapp2.view.detail.dashboard.DetailActivity
import com.muflihunnisa.githubapp2.view.home.MainAdapter
import com.muflihunnisa.githubapp2.view.home.OnItemClickCallback

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteViewBinding : ActivityFavoriteBinding
    private lateinit var favoriteViewModel : FavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteViewBinding.root)
        customAppBar()
        setViewModelProvider()
        observeData()
        showRecyclerView()
    }

    private fun showRecyclerView() {
        favoriteViewBinding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = MainAdapter(mutableListOf())
        }
    }

    private fun observeData() {
        favoriteViewModel.favoriteLiveData.observe(this, {
            if (it?.isEmpty() == true){
                favoriteViewBinding.apply {
                    ivErrorFav.visibility = View.VISIBLE
                    rvFavorite.visibility = View.GONE
                }
            }else{
                favoriteViewBinding.apply {
                    ivErrorFav.visibility = View.GONE
                    rvFavorite.visibility = View.VISIBLE

                    val mainAdapter = MainAdapter(it?.map { detailUserResponse ->
                        ItemsItem(detailUserResponse.login, type = "", detailUserResponse.avatar_url ?: "",)
                    })
                    mainAdapter.setItemClickCallback(object : OnItemClickCallback{
                        override fun onItemClicked(user: ItemsItem?) {
                            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_DATA, user)
                            startActivity(intent)
                        }

                    })
                    favoriteViewBinding.rvFavorite.adapter = mainAdapter
                }
            }
        })
    }

    private fun setViewModelProvider() {
        favoriteViewModel = ViewModelProvider(this)[FavViewModel::class.java]
    }

    private fun customAppBar() {
        title = "Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
    }

    companion object{
        fun getLaunchService(from: Context) =
            Intent(from, FavoriteActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            }
    }
}