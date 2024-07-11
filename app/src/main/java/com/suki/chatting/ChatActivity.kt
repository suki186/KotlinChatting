package com.suki.chatting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.suki.chatting.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    lateinit var adapter: MsgAdapter

    lateinit var mAuth: FirebaseAuth // 인증서비스 객체
    private lateinit var mDB: DatabaseReference // 데이터베이스 객체
    private lateinit var msgList: ArrayList<Message> // 메세지 리스트 객체

    // 대화할 상대의 이름과 UID
    private lateinit var otherName: String
    private lateinit var otherUid: String

    // 상대방 채팅방과 나의 채팅방
    private lateinit var otherRoom: String // 받는
    private lateinit var myRoom: String // 보내는

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance() // 인증 서비스 객체 초기화
        mDB = FirebaseDatabase.getInstance().reference // 데이터베이스 객체 초기화
        msgList = ArrayList() // 메세지 리스트 초기화
        adapter = MsgAdapter(this, msgList) // 어댑터 초기화 (context, ArrayList)

        // 채팅방에서 보여지는 화면
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this) // 레이아웃 설정
        binding.chatRecyclerView.adapter = adapter // 어댑터 설정

        // 데이터를 받아 otherName, otherUid에 담기 ("key값")
        otherName = intent.getStringExtra("nickname").toString()
        otherUid = intent.getStringExtra("uid").toString()

        // 액션바에 상대 이름 보이기
        supportActionBar?.title = otherName

        // 현재 접속자(보내는 사람)의 UID
        val myUid = mAuth.currentUser?.uid

        // 나의 채팅방 변수 (DB에 저장될 형태)
        myRoom = otherUid + "-" + myUid

        // 상대방 채팅방 (DB에 저장될 형태)
        otherRoom = myUid + "-" + otherUid

        // 전송 버튼 이벤트
        binding.sendMsgBtn.setOnClickListener {
            // msgObject 변수에 Message 클래스 담기
            val msg = binding.messageText.text.toString()
            val msgObject = Message(msg, myUid)

            // 메세지를 DB에 저장 (위치: DB/chats/other->my/messages)
            mDB.child("chats").child(myRoom).child("message").push()
                .setValue(msgObject).addOnSuccessListener { // 저장 성공
                    // 메세지를 상대방 DB에 저장 (위치: DB/chats/my->other/messages)
                    mDB.child("chats").child(otherRoom).child("message").push()
                        .setValue(msgObject)
                }

            binding.messageText.setText("") // 입력창 초기화
        }
        
        // -------------- 메세지를 화면에 보이기
        // DB에서 메세지 정보 가져오기
        mDB.child("chats").child(myRoom).child("message")
            .addValueEventListener(object: ValueEventListener{

                // 데이터 변경되면 실행
                override fun onDataChange(snapshot: DataSnapshot) {
                    msgList.clear()

                    for(s in snapshot.children) { // snapshot의 하위 데이터를 s에 저장
                        // 현재 메세지 정보
                        val currentMsg = s.getValue(Message::class.java)
                        msgList.add(currentMsg!!) // 리스트에 담기
                    }
                    adapter.notifyDataSetChanged() // 채팅방 화면에 적용
                }

                // 데이터 취소(오류)되면 실행
                override fun onCancelled(error: DatabaseError) {
                    // 실패
                }

            })


    }
}