package com.example.bottomnavigation.yeexiaoze.general

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bottomnavigation.databinding.ChangePasswordBinding
import com.example.bottomnavigation.yeexiaoze.database.user.UserSQLiteHelper

//The whole activity is basically the same logic compared to signup activity
class ChangePassword : AppCompatActivity() {

    //Do initialization
    private lateinit var binding: ChangePasswordBinding
    private lateinit var validation: Validation

    private lateinit var sqliteHelper: UserSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChangePasswordBinding.inflate(layoutInflater)
        validation = Validation()
        setContentView(binding.root)

        sqliteHelper = UserSQLiteHelper(this)

        //Navigate to Login page
        binding.changePasswordNavigate.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        binding.changePasswordButton.setOnClickListener {
            //Do all the validation (Logic same as the Login page)
            val usernameBool = validation.errorText(
                binding.changePasswordUsernameError,
                validation(binding.changePasswordUsername)
            )
            val idBool = validation.errorText(
                binding.changePasswordIDError,
                validation(binding.changePasswordID)
            )
            val passwordBool = validation.errorText(
                binding.changePasswordPasswordError,
                validation(binding.changePasswordPassword)
            )
            val reenterBool =
                validation.errorText(
                    binding.changePasswordReenterError,
                    validation(binding.changePasswordReenter)
                )

            if (usernameBool && idBool && passwordBool && reenterBool) {
                changeUserPassword()
            }
        }

        var passwordClickTimes = 0
        var reenterClickTimes = 0

        binding.changePasswordPasswordUnhide.setOnClickListener {
            passwordClickTimes++
            showPassword(passwordClickTimes, binding.changePasswordPassword)
        }

        binding.changePasswordReenterUnhide.setOnClickListener {
            reenterClickTimes++
            showPassword(reenterClickTimes, binding.changePasswordReenter)
        }
    }

    //Do validation (Logic same as Login page)
    private fun validation(inputField: EditText): String {
        //Check the input field passed
        when (inputField) {

            binding.changePasswordUsername -> {
                //Set error text
                val field = "username"

                //Check the type of error occurred
                return if (validation.nullValueCheck(inputField.text.toString(), field) != "") {
                    validation.nullValueCheck(inputField.text.toString(), field)
                } else if (changePasswordExistenceCheck(
                        inputField.text.toString(),
                        sqliteHelper.getAttribute("username")
                    ) != ""
                ) {
                    changePasswordExistenceCheck(
                        inputField.text.toString(),
                        sqliteHelper.getAttribute("username")
                    )
                } else {
                    ""
                }
            }

            binding.changePasswordID -> {
                val field = "email or phone number"

                return if (validation.nullValueCheck(inputField.text.toString(), field) != "") {
                    validation.nullValueCheck(inputField.text.toString(), field)
                } else if (validation.formatCheck(inputField.text.toString(), field) != "") {
                    validation.formatCheck(inputField.text.toString(), field)
                } else if (emailCheck(
                        inputField.text.toString(),
                        binding.changePasswordUsername.text.toString()
                    ) != ""
                ) {
                    emailCheck(
                        inputField.text.toString(),
                        binding.changePasswordUsername.text.toString()
                    )
                } else {
                    ""
                }
            }

            binding.changePasswordPassword -> {
                val field = "password"

                return if (validation.nullValueCheck(inputField.text.toString(), field) != "") {
                    validation.nullValueCheck(inputField.text.toString(), field)
                } else if (validation.sizeCheck(inputField.text.toString(), field) != "") {
                    validation.sizeCheck(inputField.text.toString(), field)
                } else {
                    ""
                }
            }

            binding.changePasswordReenter -> {
                val field = "re-entered password"

                return if (validation.nullValueCheck(inputField.text.toString(), field) != "") {
                    validation.nullValueCheck(inputField.text.toString(), field)
                } else if (matchPassword(inputField.text.toString()) != "") {
                    matchPassword(inputField.text.toString())
                } else {
                    ""
                }
            }

            else -> {}
        }

        return ""
    }

    //Check if password matches the reentered password
    private fun matchPassword(reenteredPassword: String): String {
        return if (reenteredPassword != binding.changePasswordPassword.text.toString()) {
            "Please ensure that the reentered password is the same as the password."
        } else {
            ""
        }
    }

    private fun showPassword(clickTimes: Int, passwordField: EditText) {
        //If user click the eye button
        if (clickTimes % 2 == 1) {
            //Unhide button
            passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            //Hide button
            passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    private fun changeUserPassword() {
        //Update the records to database
        sqliteHelper.updateUser(
            "password",
            "${binding.changePasswordPassword.text}",
            "${binding.changePasswordUsername.text}"
        )
        Log.i("Main Activity", "${binding.changePasswordPassword.text}")
        alertDialog()
    }

    private fun alertDialog() {
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Your password has been successfully changed. Kindly navigate to the login page to login.")
        alert.setPositiveButton("OK") { _, _ ->
            startActivity(Intent(this, Login::class.java))
        }

        val dialog = alert.create()
        // Disable touch outside to dismiss dialog
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun changePasswordExistenceCheck(inputText: String, attributeList: ArrayList<String>): String {
        for (i in attributeList) {
            if (inputText == i) {
                return ""
            }
        }

        //Only reachable if no record matches
        return "Please ensure that your email or phone number exists."
    }

    private fun emailCheck(contactInfo: String, username: String): String {
        try {
            if (sqliteHelper.conditionalGetAttribute(
                    "contactInfo",
                    "username", username
                ) != contactInfo
            ) {
                return "Your login information is invalid. Please enter your password again."
            }
            //If cannot find any record
        } catch (ex: Exception) {
            return "Your login information is invalid. Please enter your password again."
        }
        return ""
    }
}