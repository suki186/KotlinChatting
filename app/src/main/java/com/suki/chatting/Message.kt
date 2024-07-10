package com.suki.chatting

data class Message(
    var message: String?, // 보낸 메세지
    var sendId: String? // 보낸 사람의 UID
){
    constructor():this("", "")
}
