package com.suki.chatting

import android.app.Activity
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
import com.suki.chatting.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    lateinit var binding: ActivityLogInBinding
    lateinit var mAuth: FirebaseAuth // 인증 서비스 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater) // binding 객체 초기화

        enableEdgeToEdge()
        setContentView(binding.root)
        // setContentView(R.layout.activity_log_in) // xml 파일

        mAuth = Firebase.auth // 인증 서비스 초기화

        // 뷰바인딩 설정을 했기 때문에 버튼에 접근하기 위한 객체를 만들지 않아도 됨.
        // 로그인 버튼 이벤트
        binding.loginBtn.setOnClickListener{
            val email = binding.emailText.text.toString().trim()
            val password = binding.passwordText.text.toString().trim()

            login(email, password) // 로그인 함수
        }

        // 회원가입 버튼 이벤트
        binding.SignupBtn.setOnClickListener{
            // LogInActivity -> SignUpActivity 이동
            val intent: Intent = Intent(this@LogInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // 로그인
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 로그인 성공
                    Toast.makeText(
                        baseContext,
                        "로그인 성공!",
                        Toast.LENGTH_SHORT,
                    ).show()

                    // MainActivity로 전환
                    val intent: Intent = Intent(this@LogInActivity, MainActivity::class.java)
                    startActivity(intent)

                } else { // 로그인 실패
                    Toast.makeText(
                        baseContext,
                        "등록되지 않은 회원입니다.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d("LogIn Error", ": ${task.exception}")

                }
            }
    }


}