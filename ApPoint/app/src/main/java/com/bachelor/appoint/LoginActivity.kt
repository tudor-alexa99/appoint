package com.bachelor.appoint

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityLoginBinding
import com.bachelor.appoint.model.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // This is the new ViewBinding method
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Go to register button
        binding.tvGoToRegister.setOnClickListener {
            Log.d("onClickListener", "tv_Register clicked")
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        // Login button
        val et_email = binding.etLoginEmail
        val et_password = binding.etLoginPassword

        binding.btnLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter an email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(
                    et_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter a password",
                        Toast.LENGTH_SHORT
                    ).show();
                }
                else -> {
                    val email: String = et_email.text.toString().trim { it <= ' ' }
                    val password: String =
                        et_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "You are logged in!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                FirestoreClass().getUserDetails(this@LoginActivity)
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

        }

    }

    fun userLoggedInSuccess(user: User) {
        Log.i("Full name", user.fullName)
        Log.i("Email", user.email)

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }
}
