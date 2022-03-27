package com.github.adizcode.lookout.model.data

data class SubmitOtpBody(
    val email: String,
    val otp: String,
    val teacherEmail: String,
    val userId: String
)