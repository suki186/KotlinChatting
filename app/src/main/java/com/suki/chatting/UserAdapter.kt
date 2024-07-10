package com.suki.chatting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// user_list와 user 데이터를 연결해주는 어댑터 class
// GetUserVeiw를 상속받음
class UserAdapter(private val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.GetUserView>(){


    // ----------- user_list 화면과 연결 ---------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetUserView {
        // user_list를 view에 넣음 -> view를 GetUserView 생성자에 넣음 -> view가 itemView에 들어감 -> TextView에 접근
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false)

        return GetUserView(view)
    }

    // ----------- 데이터의 개수 return -----------------------------
    override fun getItemCount(): Int {
        return userList.size
    }

    // ----------- 데이터를 전달받아 user_list 화면에 보여줌 -----------
    override fun onBindViewHolder(holder: GetUserView, position: Int) {

        val currentUser = userList[position] // userList에 있는 데이터를 순서대로 currentUser에 넣음
        holder.nameText.text = currentUser.nickname // currentUser에 들어온 nickname을 TextView에 넣음

        // 채팅목록 클릭 이벤트 (채팅방 화면으로 이동)
        holder.itemView.setOnClickListener {

            val intent = Intent(context, ChatActivity::class.java)
            // 넘길 데이터 담기
            intent.putExtra("nickname", currentUser.nickname)
            intent.putExtra("uid", currentUser.uId)

            context.startActivity(intent) // 데이터를 담아 ChatActivity로 전송
        }
    }

    // ----------- View를 전달 받아 user_list의 TextView 객체를 만드는 class ------
    // RecyclerView, ViewHolder 상속받음
    class GetUserView(itemView: View): RecyclerView.ViewHolder(itemView) {

        // itemView에는 user_list 레이아웃 넘김.
        val nameText: TextView = itemView.findViewById(R.id.user_nickname) // TextView 객체

    }
}