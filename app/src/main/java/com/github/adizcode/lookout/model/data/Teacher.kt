package com.github.adizcode.lookout.model.data

data class Teacher(
    val __v: Int,
    val _id: String,
    val attendence: List<Attendance>,
    val createdAt: String,
    val email: String,
    val isAdmin: Boolean,
    val name: String,
    val otp: String,
    val otpHash: String,
    val password: String,
    val uid: String,
    val updatedAt: String
)