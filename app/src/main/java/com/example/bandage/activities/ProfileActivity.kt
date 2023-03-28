package com.example.bandage.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.bandage.R
import com.example.bandage.helper.LocalSharedPreferences
import com.example.bandage.models.response_model.UserResponse
import com.example.bandage.requests.UserRequests

class ProfileActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var profileDataView: View
    lateinit var emailView: TextView
    lateinit var heroLetter: TextView
    lateinit var nameView: TextView

    fun attachData(userData: UserResponse) {
        emailView.text = userData.email
        nameView.text = "${userData.firstName} ${userData.lastName}"
        heroLetter.text = userData.firstName[0].toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        progressBar = findViewById(R.id.profile_progress_Bar)
        profileDataView = findViewById(R.id.profile_data_view)
        emailView = findViewById(R.id.profile_email)
        nameView = findViewById(R.id.profile_name)
        heroLetter = findViewById(R.id.profile_hero)

        val userRequests = UserRequests(this)
        userRequests.getUserDetail()

        findViewById<TextView>(R.id.profile_logout_btn).setOnClickListener {

            LocalSharedPreferences.prefPutString(this, "token", "")

            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.profile_back_button).setOnClickListener{
            finish()
        }
    }

}