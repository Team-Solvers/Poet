package com.nkle.poet

data class Post(
    val postAuthor: String, val postTitle: String,
    val postContent: String, val id: String, val arr: ArrayList<Unit>,
    val likeCount: String, val img_url: String, val user_id: String)