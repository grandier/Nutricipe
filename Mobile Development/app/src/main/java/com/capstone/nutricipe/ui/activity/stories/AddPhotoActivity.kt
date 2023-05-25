package com.capstone.nutricipe.ui.activity.stories

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.capstone.nutricipe.R
import com.capstone.nutricipe.data.local.Session
import com.capstone.nutricipe.data.remote.api.ApiConfig
import com.capstone.nutricipe.data.remote.model.AddImage
import com.capstone.nutricipe.databinding.ActivityAddPhotoBinding
import com.capstone.nutricipe.ui.activity.dataStore
import com.capstone.nutricipe.ui.activity.recipe.RecommendedActivity
import com.capstone.nutricipe.ui.utils.reduceFileImage
import com.capstone.nutricipe.ui.utils.rotateBitmap
import com.capstone.nutricipe.ui.utils.rotateFile
import com.capstone.nutricipe.ui.utils.uriToFile
import com.capstone.nutricipe.ui.viewmodel.AddPhotoViewModel
import com.capstone.nutricipe.ui.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding
    private lateinit var addStoryViewModel: AddPhotoViewModel
    private var getFile: File? = null

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERA_X_RESULT && result.data != null) {
            val myFile = result.data?.getSerializableExtra("picture") as? File
            if (myFile != null) {
                getFile = myFile

                val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as? Boolean
                val resultBitmap = isBackCamera?.let {
                    rotateBitmap(
                        BitmapFactory.decodeFile(myFile.path), it
                    )
                }

                // Set the captured image bitmap to the shapeableImageView
                binding.shapeableImageView.setImageBitmap(resultBitmap)
                binding.shapeableImageView.visibility = View.VISIBLE
            } else {
                // Handle the case when the captured image file is null or empty
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val selectedImg: Uri = result.data?.data ?: return@registerForActivityResult
            val myFile = uriToFile(selectedImg, this@AddPhotoActivity)
            if (myFile != null) {
                getFile = myFile
                binding.shapeableImageView.setImageURI(selectedImg)
                binding.shapeableImageView.visibility = View.VISIBLE
            } else {
                // Handle the case when the selected image file is null or empty
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                showText()
                finish()
            }
        }
    }

    private fun showText() {
        Toast.makeText(
            this,
            "Tidak mendapatkan permission.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun allPermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = Session.getInstance(dataStore)
        addStoryViewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[AddPhotoViewModel::class.java]

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.findRecipe.isEnabled = false

        binding.edTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Not needed for this case
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Not needed for this case
            }

            override fun afterTextChanged(p0: Editable?) {
                val isTitleEmpty = p0.isNullOrBlank()
                val isDescriptionEmpty = binding.edDescription.text.isNullOrBlank()
                val isTitleExceeded = (p0?.length ?: 0) > 28

                if (isTitleExceeded) {
                    val maxLength = 28
                    val truncatedText = p0?.subSequence(0, maxLength)
                    binding.edTitle.setText(truncatedText)
                    binding.edTitle.setSelection(maxLength)
                }

                binding.findRecipe.isEnabled = !isTitleEmpty && !isDescriptionEmpty

                if (isTitleEmpty) {
                    binding.edTitle.error = getString(R.string.fill_in)
                } else {
                    binding.edTitle.error = null
                }
            }
        })


        binding.edDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this case
            }

            override fun afterTextChanged(s: Editable?) {
                val maxLines = 4
                val lineCount = binding.edDescription.lineCount

                if (lineCount > maxLines) {
                    val lastLineIndex = binding.edDescription.layout.getLineForVertical(binding.edDescription.height) - 1
                    val lastVisibleCharIndex = binding.edDescription.layout.getLineVisibleEnd(lastLineIndex)
                    s?.delete(lastVisibleCharIndex, s.length)
                }

                val isDescriptionEmpty = s.isNullOrBlank()
                val isTitleEmpty = binding.edTitle.text.isNullOrBlank()
                binding.findRecipe.isEnabled = !isDescriptionEmpty && !isTitleEmpty

                if (isDescriptionEmpty) {
                    binding.edDescription.error = getString(R.string.fill_in)
                } else {
                    binding.edDescription.error = null
                }
            }
        })



        binding.btnTakePicture.setOnClickListener {
            startCameraX()
        }

        binding.btnOpenGallery.setOnClickListener {
            startGallery()
        }

        binding.findRecipe.setOnClickListener {
            if (getFile != null) {
                findRecipe()
            } else {
                binding.findRecipe.isEnabled = false
                Toast.makeText(this, getString(R.string.choose_picture), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun findRecipe() {
        showLoading(true)
        val file = getFile ?: run {
            Toast.makeText(this, getString(R.string.choose_picture), Toast.LENGTH_SHORT).show()
            return
        }
        reduceFileImage(file)
        val text1= binding.edTitle.text.toString().takeIf { it.isNotEmpty() } ?: " "
        val text = binding.edDescription.text.toString().takeIf { it.isNotEmpty() } ?: " "
        val title = text1.toRequestBody("text/plain".toMediaType())
        val description = text.toRequestBody("text/plain".toMediaType())

        // Rotate the file before uploading
        rotateFile(file, isBackCamera = true)

        // Create a MultipartBody.Part for uploading the image
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        addStoryViewModel.getToken().observe(this) { token ->
            val service = ApiConfig.getApiService().uploadImage("Bearer $token", imageMultipart, title, description)
            service.enqueue(object : Callback<AddImage> {
                override fun onResponse(call: Call<AddImage>, response: Response<AddImage>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val addImage = response.body()

                        if (addImage != null && addImage.error == false && addImage.message == "success") {
                            val idHistory = addImage.idHistory

                            Toast.makeText(
                                this@AddPhotoActivity,
                                getString(R.string.success_upload),
                                Toast.LENGTH_SHORT
                            ).show()

                            // Save the idHistory to SharedPreferences or wherever you want
                            // For example, using SharedPreferences:
                            val sharedPref = getSharedPreferences("YOUR_PREF_NAME", Context.MODE_PRIVATE)
                            sharedPref.edit().putString("idHistory", idHistory).apply()

                            if (idHistory != null) {
                                val intent = Intent(this@AddPhotoActivity, RecommendedActivity::class.java)
                                intent.putExtra("idHistory", idHistory) // Pass idHistory as an extra
                                startActivity(intent)
                                finish()
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            } else {
                                // Handle the case when idHistory is null
                                Toast.makeText(
                                    this@AddPhotoActivity,
                                    "idHistory is null",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@AddPhotoActivity,
                                "Upload failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@AddPhotoActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AddImage>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(
                        this@AddPhotoActivity,
                        getString(R.string.failed_retrofit),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}