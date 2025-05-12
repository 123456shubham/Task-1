package com.example.myapplication.model

data class MyError (val location: String,
                    val msg: String,
                    val `param`: String,
                    val value: Int,
                    val message: String)