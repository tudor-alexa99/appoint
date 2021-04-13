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
import com.bachelor.appoint.databinding.ActivityLoginBinding
import com.bachelor.appoint.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        Log.d("Create bind", binding.toString())
        val view = binding.root

        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Go to Login button
        binding.tvGoToLogin.setOnClickListener {
            Log.d("onClickListener", "tv_Login clicked")
            intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Register button
        val et_name = binding.etFullName
        val et_birthday = binding.etDate
        val et_email = binding.etRegisterEmail
        val et_password = binding.etRegisterPassword

        binding.btnRegister.setOnClickListener {
            // Validate fields
            when {
                // Fullname
                TextUtils.isEmpty(et_name.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your full name",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Email
                TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter an email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Birthday
                TextUtils.isEmpty(et_birthday.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter your birthday",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Password
                TextUtils.isEmpty(
                    et_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter a password",
                        Toast.LENGTH_SHORT
                    ).show();
                }
                else -> {
                    val fullName: String = et_name.text.toString()
                    val email: String = et_email.text.toString().trim { it <= ' ' }
                    val birthday: String = et_birthday.text.toString().trim() { it <= ' '}
                    val password: String =
                        et_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // if the registration is successful
                            if (task.isSuccessful) {
                                // Firebase registered user
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "You have registered successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", firebaseUser.email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

        }

    }
}