package com.example.museumm

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userDatabaseReference: DatabaseReference

    private var fullnameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null

    private var submitButton: Button? = null
    private var cancelButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        // widget pairing...
        fullnameEditText = findViewById(R.id.fullnameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        submitButton = findViewById(R.id.submitButton)
        cancelButton = findViewById(R.id.cancelButton)

        submitButton!!.setOnClickListener(this)
        cancelButton!!.setOnClickListener(this)
    }

    // Click Listener Method
    override fun onClick(v: View) {
        when (v.id) {
            R.id.submitButton -> {

                val inputFulname = fullnameEditText?.text.toString().trim()
                val inputEmail = emailEditText?.text.toString().trim()
                val inputPassword = passwordEditText?.text.toString().trim()
                val inputConfirmPassword = confirmPasswordEditText?.text.toString().trim()

                if (!inputFulname.isNullOrBlank() && !inputEmail.isNullOrBlank() && !inputPassword.isNullOrBlank() && !inputConfirmPassword.isNullOrBlank()) {

                    if(Valid.isValidEmail(inputEmail)){
                        if (inputPassword.equals(inputConfirmPassword)) {
                            submitButton?.setText("Carregando")
                            submitButton?.setEnabled(false)

                            // check if already exists?
                            userDatabaseReference.orderByChild("email").equalTo(inputEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    var userEmail : String? = null

                                    for (userSnapshot in dataSnapshot.children) {
                                        val user = userSnapshot.getValue(User::class.java)
                                        userEmail = user!!.email

                                        if(inputEmail == userEmail){
                                            break
                                        }
                                    }

                                    // no match to existing users
                                    if(userEmail == null) {
                                        val user = User(inputFulname, inputEmail, inputPassword)
                                        var userRef = userDatabaseReference.push()
                                        userDatabaseReference.child(userRef.key.toString()).setValue(user, object : DatabaseReference.CompletionListener {
                                            override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {

                                                submitButton?.setText(R.string.submit)
                                                submitButton?.setEnabled(true)

                                                if (p0 != null) {
                                                    Toast.makeText(this@SignUpActivity, p0.message, Toast.LENGTH_SHORT).show()
                                                } else {
                                                    val builder = AlertDialog.Builder(this@SignUpActivity)
                                                    builder.setTitle(android.R.string.dialog_alert_title)
                                                    builder.setMessage(R.string.register_success)
                                                    builder.setPositiveButton(R.string.login_now) { dialog, which ->

                                                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                                                        intent.putExtra("logged_in_email", inputEmail)
                                                        startActivity(intent)

                                                        finish()
                                                    }
                                                    builder.show()
                                                }
                                            }
                                        })
                                    }else{
                                        if(submitButton?.isEnabled == false){
                                            Toast.makeText(
                                                this@SignUpActivity,
                                                "Email address is not available.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    submitButton?.setText(R.string.submit)
                                    submitButton?.setEnabled(true)
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Getting user failed
                                    val builder = AlertDialog.Builder(this@SignUpActivity)
                                    builder.setTitle(android.R.string.dialog_alert_title)
                                    builder.setMessage(databaseError.message)
                                    builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                                        Toast.makeText(this@SignUpActivity,
                                            android.R.string.yes, Toast.LENGTH_SHORT).show()
                                    }
                                    builder.show()

                                    submitButton?.setText(R.string.submit)
                                    submitButton?.setEnabled(true)
                                }
                            })
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Confirm password and password do not match.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            this@SignUpActivity,
                            "Invalid email address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    Toast.makeText(this@SignUpActivity, "All input values are reguired!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.cancelButton -> {
                finish()
            }
        }
    }
}

