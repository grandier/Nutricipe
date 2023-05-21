package com.capstone.nutricipe.ui.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.databinding.ActivityProfileBinding
import com.capstone.nutricipe.ui.activity.authentication.LoginActivity
import com.capstone.nutricipe.ui.viewmodel.ProfileViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutricipe.R
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
                        showLoading(true)
                        ApiConfig.getApiService().updateName("Bearer $token", newName)
                            .enqueue(object : Callback<Profile> {
                                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                                    if (response.isSuccessful) {
                                        // Handle the successful response
                                        val updatedProfile = response.body()?.data
                                        if (updatedProfile != null) {
                                            // Update the name in the UI and ViewModel
                                            binding.tvName.text = updatedProfile.name
                                        }
                                    } else {
                                        // Handle the error response
                                        // Show an error message or perform error handling
                                    }
                                    showLoading(false)
                                }

                                override fun onFailure(call: Call<Profile>, t: Throwable) {
                                    // Handle the failure case
                                    // Show an error message or perform error handling
                                    showLoading(false)
                                }
                            })
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