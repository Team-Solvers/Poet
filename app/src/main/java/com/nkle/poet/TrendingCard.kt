package com.nkle.poet

data class TrendingCard(val imagePath : String,val poetName : String, val id: String,
                        val likes: ArrayList<*>,val poems: Int,
                        val user_id: String,
                        val password: String,
//                        val likes: Array
                        )