package com.suki.chatting

// 사용자 한명의 데이터를 담는 class
data class User(
    var nickname: String,
    var email: String,
    var uId: String
){
    constructor(): this("", "", "")
}
