package com.capstone.nutricipe.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.paging.adapter.LoadingStateAdapter
import com.capstone.nutricipe.data.paging.adapter.PhotoAdapter
import com.capstone.nutricipe.databinding.ActivityMainBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.activity.profile.ProfileActivity
import com.capstone.nutricipe.ui.activity.stories.AddPhotoActivity
import com.capstone.nutricipe.ui.viewmodel.MainViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = Session.getInstance(dataStore)

        mainViewModel = ViewModelProvider(
            this, ViewModelFactory(pref, this)
        )[MainViewModel::class.java]

        mainViewModel.getToken().observe(this) { token: String ->
            if (token.isNotEmpty()) {
                getPhotoPage(token)

            } else if (token.isEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.message.observe(this) {
            if (it == "Failure") {
                Toast.makeText(this, R.string.failed_retrofit, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.acceptance.observe(this) {
            if (it) {
                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddPhoto.setOnClickListener {
            val intent = Intent(this, AddPhotoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.ivProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getPhotoPage(token: String) {
        val adapter = PhotoAdapter()
        binding.rvHistory.layoutManager =
            LinearLayoutManager(this@MainActivity) // Set the LinearLayoutManager
        binding.rvHistory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.getPhoto(token)
            .observe(this) { photoData ->
                adapter.submitData(lifecycle, photoData)
            }
    }

    override fun onBackPressed() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeIntent)
    }
}