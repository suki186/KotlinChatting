package com.suki.chatting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.suki.chatting.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater) // binding 객체 초기화
        setContentView(binding.root)


//        enableEdgeToEdge()
//        setContentView(R.layout.activity_log_in) // xml 파일

        // 뷰바인딩 설정을 했기 때문에 버튼에 접근하기 위한 객체를 만들지 않아도 됨.
        // 회원가입 버튼 이벤트
        binding.SignupBtn.setOnClickListener{
            // LogInActivity -> SignUpActivity 이동
            val intent: Intent = Intent(this@LogInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    lateinit var binding: ActivityLogInBinding
}