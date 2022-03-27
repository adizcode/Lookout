package com.github.adizcode.lookout.model.data

data class Attendance(
    val _id: String,
    val active: Boolean,
    val date: String,
    val studentPresent: Int,
    val students: List<String>
)