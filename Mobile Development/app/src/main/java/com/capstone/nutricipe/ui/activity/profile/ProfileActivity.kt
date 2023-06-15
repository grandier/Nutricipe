package com.capstone.nutricipe.ui.activity.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.databinding.ActivityProfileBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.viewmodel.ProfileViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.paging.adapter.LoadingStateAdapter
import com.capstone.nutricipe.data.paging.adapter.HistoryAdapter
import com.capstone.nutricipe.databinding.CardDialogBinding
import com.capstone.nutricipe.databinding.DialogRenameBinding
import com.capstone.nutricipe.ui.activity.dataStore


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = Session.getInstance(dataStore)

        profileViewModel = ViewModelProvider(
            this, ViewModelFactory(pref, this)
        )[ProfileViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
            lenguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            logout.setOnClickListener {
                logoutDialog()
            }
        }

        profileViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                profileViewModel.getProfile(token)
                getPhotoPage(token)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        profileViewModel.profile.observe(this) { profile ->
            if (profile != null) {
                binding.tvEmail.text = profile.email
                binding.tvName.text = profile.name
            }
        }

        profileViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        profileViewModel.message.observe(this) {
            if (it.isNotEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.editButton.setOnClickListener {
            val dialogBinding = DialogRenameBinding.inflate(layoutInflater)
            val dialogView = dialogBinding.root

            val dialogBuilder = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Rename Name")

            val alertDialog = dialogBuilder.create()
            alertDialog.show()

            dialogBinding.btnRename.setOnClickListener {
                val newName = dialogBinding.etNewName.text.toString()

                profileViewModel.getToken().observe(this) { token ->
                    if (token.isNotEmpty()) {
                        profileViewModel.updateProfile(token, newName)
                    } else {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                alertDialog.dismiss()
            }
        }

    }

    private fun logoutDialog() {
        val dialogBinding = CardDialogBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root

        dialogBinding.tvTitleDialog.text = getString(R.string.title_logout)
        dialogBinding.tvTextDialog.text = getString(R.string.text_logout)
        dialogBinding.btnNo.text = getString(R.string.no)
        dialogBinding.btnYes.text = getString(R.string.yes)

        val dialogBuilder = Dialog(this)
        dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setContentView(dialogView)
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBuilder.show()

        dialogBinding.btnYes.setOnClickListener {
            logout()
        }
        dialogBinding.btnNo.setOnClickListener {
            dialogBuilder.dismiss()
        }
    }

    private fun getPhotoPage(token: String) {
        val adapter = HistoryAdapter()
        binding.rvHistory.setHasFixedSize(true)
        binding.rvHistory.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvHistory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        // Initialize SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Clear the current data and fetch new data
            adapter.refresh()
        }

        profileViewModel.getPhoto(token)
            .observe(this) { photoData ->
                adapter.submitData(lifecycle, photoData)

                // Stop the swipe refresh animation
                binding.swipeRefreshLayout.isRefreshing = false
            }
    }



    fun logout() {
        profileViewModel.logout()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}