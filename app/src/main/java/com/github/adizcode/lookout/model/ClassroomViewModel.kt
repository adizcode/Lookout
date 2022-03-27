package com.github.adizcode.lookout.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.adizcode.lookout.model.data.*
import com.github.adizcode.lookout.model.enums.Profile
import com.github.adizcode.lookout.model.enums.Screen
import com.github.adizcode.lookout.network.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val OTP_EXPIRATION_IN_MILLIS: Long = 300000

class ClassroomViewModel : ViewModel() {

    private var currentStudent: Student? = null
    private var currentTeacher: Teacher? = null

    var profile by mutableStateOf(Profile.NONE)
        private set

    var screen by mutableStateOf(Screen.CHOOSE_PROFILE)
        private set

    var currentOtp: Int? by mutableStateOf(null)
        private set

    var isLoading by mutableStateOf(false)

    var attendanceMarked by mutableStateOf(false)
        private set

    var loginUnsuccessful by mutableStateOf(false)
        private set

    fun goToProfileLogin(selectedProfile: Profile) {
        chooseProfile(selectedProfile)
        navigateToScreen(Screen.LOGIN)
    }

    // Doesn't implement back button behaviour
    fun navigateToScreen(destination: Screen) {
        screen = destination
        loginUnsuccessful = false
    }

    fun loginUser(userCredentials: LoginCredentials) {
        viewModelScope.launch {
            try {
                val apiService = ApiService.getInstance()
                if (profile == Profile.STUDENT) {
                    currentTeacher = null
                    currentStudent = apiService.loginStudent(userCredentials).user
                } else {
                    currentStudent = null
                    currentTeacher = apiService.loginTeacher(userCredentials).user
                }

                navigateToScreen(Screen.HOME)

            } catch (e: Exception) {
                Log.e("Error while logging in", e.toString())
                loginUnsuccessful = true
            }
        }
    }

    fun generateOtp() {

        if (currentTeacher == null) {
            Log.e("Error while generating OTP", "No teacher found")
            return
        }

        // Display loader
        isLoading = true

        viewModelScope.launch {
            try {
                val apiService = ApiService.getInstance()
                Log.d("OTP", currentTeacher!!.email)
                val response = apiService.generateOtp(TeacherEmail(currentTeacher!!.email))
                setOtpReceived(response.otp)

            } catch (e: Exception) {
                // Inform the user
                Log.e("Error while generating OTP", e.toString())
            }

            // Stop displaying loader
            isLoading = false
        }
    }

    private fun setOtpReceived(otpReceived: Int) {
        currentOtp = otpReceived
        expireOtpAfterDuration()
    }

    fun submitOtp(otpToSubmit: String) {
        if (currentStudent == null) {
            Log.e("Error while submitting OTP", "No student found")
            return
        }

        val otpBody = SubmitOtpBody(
            email = currentStudent!!.email,
            otp = otpToSubmit,
            // TODO: Currently not received in any response
            teacherEmail = "20bcs9417@cuchd.in",
            userId = currentStudent!!._id
        )

        // Display loader
        isLoading = true

        viewModelScope.launch {
            try {
                val apiService = ApiService.getInstance()
                apiService.submitOtp(otpBody)

                attendanceMarked = true

            } catch (e: Exception) {
                // Inform the user
                Log.e("Error while submitting OTP", e.toString())
            }

            // Stop displaying loader
            isLoading = false
        }
    }

    fun signOut() {
        resetState()
    }

    private fun resetState() {
        currentStudent = null
        currentTeacher = null
        profile = Profile.NONE
        screen = Screen.CHOOSE_PROFILE
        currentOtp = null
        isLoading = false
        attendanceMarked = false
        loginUnsuccessful = false
    }

    private fun expireOtpAfterDuration() {
        viewModelScope.launch {
            delay(OTP_EXPIRATION_IN_MILLIS)
            currentOtp = null
        }
    }

    private fun chooseProfile(chosenProfile: Profile) {
        profile = chosenProfile
    }

}