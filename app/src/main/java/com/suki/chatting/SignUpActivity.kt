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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.*
import com.google.firebase.ktx.Firebase
import com.suki.chatting.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var mAuth: FirebaseAuth // 인증서비스 객체
    private lateinit var mDB: DatabaseReference // 데이터베이스 객체


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        mAuth = Firebase.auth // 인증 서비스 객체 초기화
        mDB = Firebase.database.reference // 데이터베이스 객체 초기화

        // 가입하기 버튼 이벤트
        binding.SignupBtn.setOnClickListener {

            val nickname = binding.nicknameText.text.toString().trim()
            val email = binding.emailText.text.toString().trim()
            val password = binding.passwordText.text.toString().trim()

            signUp(nickname, email, password) // 회원가입 함수
        }
        
    }

    // 회원가입
    private fun signUp(nickname: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 회원가입 성공
                    Toast.makeText(
                        baseContext,
                        "회원가입 성공!",
                        Toast.LENGTH_SHORT,
                    ).show()

                    // MainActivity로 전환
                    val intent: Intent = Intent(this@SignUpActivity, LogInActivity::class.java)
                    startActivity(intent)
                    
                    // 사용자 정보 저장
                    saveUser(nickname, email, mAuth.currentUser?.uid!!) // 닉네임, 이메일, 사용자인증코드(UID)

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

    // 사용자 정보를 DB에 저장
    private fun saveUser(nickname: String, email: String, uId: String) {
        // User 클래스에 데이터를 담아 Real DB에 저장
        // DB/user/고유uid에 nickname, email, uid 저장
        val user = User(nickname, email, uId)
        mDB.child("user").child(uId).setValue(user)
    }

}