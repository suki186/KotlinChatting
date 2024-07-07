package com.suki.chatting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.suki.chatting.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var mAuth: FirebaseAuth // 인증 서비스 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        // 인증 서비스 초기화
        mAuth = Firebase.auth

        // 가입하기 버튼 이벤트
        binding.SignupBtn.setOnClickListener {

            val email = binding.emailText.text.toString().trim()
            val password = binding.passwordText.text.toString().trim()

            signUp(email, password)
        }
        
    }

    // 회원가입
    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 회원가입 성공
                    Toast.makeText(
                        baseContext,
                        "회원가입 성공!",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val intent: Intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)

                } else { // 회원가입 실패
                    Toast.makeText(
                        baseContext,
                        "회원가입에 실패했습니다..",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d("SignUp Error", ": ${task.exception}")

                }
            }
    }
}