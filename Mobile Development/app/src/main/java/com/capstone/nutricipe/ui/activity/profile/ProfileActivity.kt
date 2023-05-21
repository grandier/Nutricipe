package com.capstone.nutricipe.ui.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.databinding.ActivityProfileBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.viewmodel.ProfileViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.paging.adapter.LoadingStateAdapter
import com.capstone.nutricipe.data.paging.adapter.PhotoAdapter
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.AddImage
import com.capstone.nutricipe.data.remote.model.Profile
import com.capstone.nutricipe.databinding.DialogRenameBinding
import com.capstone.nutricipe.ui.activity.dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        //Uji coba doang
        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


        binding.logout.setOnClickListener {
            showLogoutConfirmationDialog()
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

    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Logout")
        alertDialogBuilder.setMessage("Are you sure you want to logout?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            logout()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getPhotoPage(token: String) {
        val adapter = PhotoAdapter()
        binding.rvHistory.layoutManager =
            LinearLayoutManager(this@ProfileActivity) // Set the LinearLayoutManager
        binding.rvHistory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        profileViewModel.getPhoto(token)
            .observe(this) { photoData ->
                adapter.submitData(lifecycle, photoData)
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