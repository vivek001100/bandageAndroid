package com.example.bandage.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.bandage.R
import com.example.bandage.models.request_model.LoginRegisterRequest
import com.example.bandage.requests.UserRequests


class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_signup, container, false)
        var signupBtn = view.findViewById<TextView>(R.id.signup_btn)
        view.findViewById<TextView>(R.id.signup_login_btn).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_frame_layout, LoginFragment()).commit()
        }

        val userRequest = UserRequests(requireContext())

        var emailView = view.findViewById<TextView>(R.id.signup_email_edit)
        var nameView = view.findViewById<TextView>(R.id.signup_name_edit)
        var passwordView = view.findViewById<TextView>(R.id.signup_password_edit)
        var confirmPasswordView = view.findViewById<TextView>(R.id.signup_confirm_password_edit)
        var signupProgress = view.findViewById<ProgressBar>(R.id.signup_progress)


        signupBtn.setOnClickListener {

            val email = emailView.text.toString()
            val password = passwordView.text.toString()
            val name = nameView.text.toString()
            val confirmPassword = confirmPasswordView.text.toString()
            if (email.isEmpty())
                emailView.error = "required"
            else if (!email.matches(getString(R.string.email_regex).toRegex()))
                emailView.error = "Enter valid email"
             else if (name.isEmpty())
                nameView.error = "required"
            else if (password.isEmpty())
                passwordView.error = "required"
            else if (password.length < 8) {
                passwordView.error = "Password must be atleast of length 8"
            } else if (confirmPassword.isEmpty())
                confirmPasswordView.error = "required"
            else if (confirmPassword != password)
                confirmPasswordView.error = "password not match"
            else {
                val splitName = name.split(" ").toTypedArray()
                val firstName = splitName[0]
                var lastName = ""
                if (splitName.size > 1) {
                    lastName = splitName[1]
                }
                val newUser = LoginRegisterRequest(email, firstName, lastName, password)
                Log.i("signup clicked", "onCreateView: ")
                userRequest.registerUser(newUser, signupBtn, signupProgress, true)
            }

        }

        return view
    }

}