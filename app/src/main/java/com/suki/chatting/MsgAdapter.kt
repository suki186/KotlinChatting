package com.suki.chatting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

// 채팅화면과 message 데이터를 연결해주는 어댑터 class
// ViewHolder 클래스가 2개이기 때문에 둘 중 어느 View든 받을 수 있도록 RecyclerView.ViewHolder 넣음
class MsgAdapter(private val context: Context, private val msgList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        private val my = 0
        private val other = 1

    // 어떤 ViewHolder를 선택할 지
    override fun getItemViewType(position: Int): Int {
        val currentMsg = msgList[position] // 현재 메세지 값

        // 현재 사용자와 현재 메세지의 UID 비교 -> 같다면 내가 보낸 메세지
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMsg.sendId)) {
            my
        } else {
            other
        }
    }

    // ----------- my_msg / other_msg 화면과 연결 ---------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) { // 나의 화면
            val view: View = LayoutInflater.from(context).inflate(R.layout.my_msg, parent, false)
            GetMyMsgView(view)
        } else { // 상대방
            val view: View = LayoutInflater.from(context).inflate(R.layout.other_msg, parent, false)
            GetOtherMsgView(view)
        }
    }

    // ----------- 데이터의 개수 return -----------------------------
        override fun getItemCount(): Int {
        return msgList.size
    }

    // ----------- 데이터를 전달받아 my_msg / other_msg 화면에 보여줌 -----------
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = msgList[position] // msgList에 있는 데이터를 순서대로 currentMsg 넣음

        if (holder.javaClass == GetMyMsgView::class.java) {
            val holder2 = holder as GetMyMsgView // holder를 GetMyMsgView 타입으로 바꿔 담음
            holder2.myMsg.text = currentMsg.message // currentMsg 들어온 message를 TextView에 넣음
        } else {
            val holder2 = holder as GetOtherMsgView
            holder2.otherMsg.text = currentMsg.message
        }

    }

    // ----- 나의 View를 전달 받아 my_msg의 TextView 객체를 만드는 ViewHolder class
    // onCreageView()에서 전달 받음
    class GetMyMsgView(itemView: View): RecyclerView.ViewHolder(itemView) {

        val myMsg: TextView = itemView.findViewById(R.id.my_msg_text) // TextView 객체
    }

    // ----- 상대방의 View를 전달 받아 other_msg의 TextView 객체를 만드는 ViewHolder class
    class GetOtherMsgView(itemView: View): RecyclerView.ViewHolder(itemView) {

        val otherMsg: TextView = itemView.findViewById(R.id.other_msg_text) // TextView 객체
    }
}