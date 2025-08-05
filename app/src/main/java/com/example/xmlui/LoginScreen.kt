package com.example.xmlui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xmlui.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLoginScreenBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email=binding.etEmail.text.toString().trim();
            val password=binding.etPassword.text.toString().trim();
            if (checkDetails(email, password,)){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                    startActivity(Intent(this, SignUpScreen::class.java))
                    finish()
                    Toast.makeText(this,"Login successfully", Toast.LENGTH_SHORT).show()

                }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this,"Login failure ${exception.message}", Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }
    private fun checkDetails(email: String, password: String): Boolean {
        var isValid=true
        if (email.isEmpty()) {
            binding.etEmail.error="Error"
            isValid=false
        }
        if (password.isEmpty() || password.length<6){
            binding.etPassword.error="Error"
            isValid= false
        }
        return isValid
    }

}