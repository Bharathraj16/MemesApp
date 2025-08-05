package com.example.xmlui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.xmlui.databinding.ActivitySignUpScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpScreen : AppCompatActivity() {
    private  lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySignUpScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        firebaseAuth= FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            val email=binding.etEmail.text.toString().trim();
            val password=binding.etPassword.text.toString().trim();
            val confirmPassword=binding.etConfirmPassword.text.toString().trim();
            if (checkDetails(email, password, confirmPassword)){
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                    startActivity(Intent(this, LoginScreen::class.java))
                    finish()
                    Toast.makeText(this,"SignUp successfully", Toast.LENGTH_SHORT).show()

                 }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this,"SignUp failure ${exception.message}", Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }
    private fun checkDetails(email: String, password: String, confirmPassword: String): Boolean {
        var isValid=true
        if (email.isEmpty()) {
            binding.etEmail.error="Error"
            isValid=false
        }
        if (password.isEmpty() || password.length<6){
            binding.etPassword.error="Error"
            isValid= false
        }
        if (confirmPassword.isEmpty() || !confirmPassword.equals(password)){
            binding.etConfirmPassword.error="Error"
            isValid= false
        }
        return isValid
    }
}


