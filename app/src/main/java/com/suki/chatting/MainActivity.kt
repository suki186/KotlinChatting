package com.suki.chatting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.suki.chatting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: UserAdapter

    private lateinit var mAuth: FirebaseAuth // 인증서비스 객체
    private lateinit var mDB: DatabaseReference // 데이터베이스 객체
    private lateinit var userList: ArrayList<User> // 사용자 리스트 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)

        mAuth = Firebase.auth // 인증 서비스 객체 초기화
        mDB = Firebase.database.reference // 데이터베이스 객체 초기화
        userList = ArrayList() // 사용자 리스트 초기화
        adapter = UserAdapter(this, userList) // 어댑터 초기화 (context, ArrayList)

        // activity_main에서 보여지는 화면
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this) // 레이아웃 설정
        binding.userRecyclerView.adapter = adapter // 어댑터 설정

        // realtime database에서 사용자 정보 가져오기
        mDB.child("user").addValueEventListener(object:ValueEventListener{

            // 데이터 변경되면 실행
            override fun onDataChange(snapshot: DataSnapshot) {
                for(s in snapshot.children) { // snapshot의 하위 데이터를 s에 저장
                    // 현재 사용자 정보
                    val currentUser = s.getValue(User::class.java)
                    // 본인을 제외한 사용자들만 보임 (현재사용자의 uid 제외)
                    if(mAuth.currentUser?.uid != currentUser?.uId) {
                        userList.add(currentUser!!) // 리스트에 담기
                    }
                }
                adapter.notifyDataSetChanged() // main 화면에 적용
            }

            // 데이터 취소(오류)되면 실행
            override fun onCancelled(error: DatabaseError) {
                // 실패
            }

        })
    }

    // 화면에 메뉴바 추가
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 로그아웃 아이템 선택 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logOut) {
            mAuth.signOut() // 인증서비스 로그아웃

            // LoginActivity로 이동
            val intent = Intent(this@MainActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(
                baseContext,
                "로그아웃",
                Toast.LENGTH_SHORT,
            ).show()

            return true
        }
        return true
    }
}