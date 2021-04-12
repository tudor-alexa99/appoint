package com.bachelor.appoint

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.bachelor.appoint.databinding.ActivityLoginBinding
import com.bachelor.appoint.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        Log.d("Create bind", binding.toString())
        val view = binding.root

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        binding.tvGoToLogin.setOnClickListener {
            Log.d("onClickListener", "tv_Login clicked")
            intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener{
            Log.d("onCreate", "Button Clicked")
        }

        setContentView(view)
    }
}