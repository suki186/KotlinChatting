package com.suki.chatting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.suki.chatting.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    lateinit var mAuth: FirebaseAuth // 인증서비스 객체
    private lateinit var mDB: DatabaseReference // 데이터베이스 객체

    // 대화할 상대의 이름과 UID
    private lateinit var otherName: String
    private lateinit var otherUid: String

    // 상대방 채팅방과 나의 채팅방
    private lateinit var otherRoom: String // 받는
    private lateinit var myRoom: String // 보내는

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        // 데이터를 받아 otherName, otherUid에 담기 ("key값")
        otherName = intent.getStringExtra("nickname").toString()
        otherUid = intent.getStringExtra("uid").toString()

        // 액션바에 상대 이름 보이기
        supportActionBar?.title = otherName

        // 현재 접속자(보내는 사람)의 UID
        val myUid = mAuth.currentUser?.uid


    }
}