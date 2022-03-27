package com.github.adizcode.lookout.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.adizcode.lookout.model.ClassroomViewModel
import com.github.adizcode.lookout.ui.theme.Arapawa
import com.mukesh.OTP_VIEW_TYPE_BORDER
import com.mukesh.OtpView

@Composable
fun TeacherHome(viewModel: ClassroomViewModel) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        if (viewModel.currentOtp == null) {
            Button(onClick = { viewModel.generateOtp() }) {
                Text("Generate OTP", fontSize = 18.sp)
            }
        } else {
            Spacer(Modifier.height(40.dp))

            Card(
                backgroundColor = Color.White,
                contentColor = Arapawa,
                elevation = 5.dp
            ) {
                Column(Modifier.padding(32.dp)) {
                    Text("Ongoing Session", style = MaterialTheme.typography.h4)
                    Spacer(Modifier.height(20.dp))
                    Text("OTP: " + viewModel.currentOtp, style = MaterialTheme.typography.subtitle1)
                    Text("Expires in 5 minutes", style = MaterialTheme.typography.subtitle2)
                }
            }
        }
    }
}

@Composable
fun StudentHome(viewModel: ClassroomViewModel) {

    val (otpVal, setOtpVal) = rememberSaveable { mutableStateOf("") }

    if (viewModel.attendanceMarked)
        Text("Attendance Marked!", style = MaterialTheme.typography.h5)
    else
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OtpView(
                otpText = otpVal,
                onOtpTextChange = setOtpVal,
                type = OTP_VIEW_TYPE_BORDER,
                password = false,
                containerSize = 48.dp,
                charColor = MaterialTheme.colors.primary,
                otpCount = 6
            )
            Spacer(Modifier.height(40.dp))
            Button(onClick = { viewModel.submitOtp(otpVal) }) {
                Text("Submit OTP", fontSize = 18.sp)
            }
        }
}