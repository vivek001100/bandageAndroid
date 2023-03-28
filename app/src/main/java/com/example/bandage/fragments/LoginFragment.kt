package com.example.bandage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import com.example.bandage.R
import com.example.bandage.models.request_model.LoginRegisterRequest
import com.example.bandage.requests.UserRequests

class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_login, container, false)
        view.findViewById<TextView>(R.id.login_new_user_btn).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_frame_layout, SignupFragment()).commit()
        }

        var loginBtn = view.findViewById<TextView>(R.id.login_btn)


        val userRequest = UserRequests(requireContext())

        var emailView = view.findViewById<TextView>(R.id.login_email_edit)
        var passwordView = view.findViewById<TextView>(R.id.login_password_edit)
        var signupProgress = view.findViewById<ProgressBar>(R.id.login_progress)
        val isLoginSaved = view.findViewById<CheckBox>(R.id.login_remember_me_check_box)

        loginBtn.setOnClickListener {

            val email = emailView.text.toString()
            val password = passwordView.text.toString()

            if (email.isEmpty())
                emailView.error = "required"
            else if (!email.matches(getString(R.string.email_regex).toRegex()))
                emailView.error = "Enter valid email"
            else if (password.isEmpty())
                passwordView.error = "required"
            else {

                val newUser = LoginRegisterRequest(email, null, null, password)
                userRequest.loginUser(newUser, loginBtn, signupProgress, isLoginSaved.isChecked)
            }

        }


        return view
    }


}