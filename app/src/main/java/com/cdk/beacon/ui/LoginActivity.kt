package com.cdk.beacon.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.cdk.beacon.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.textResource
import org.jetbrains.anko.toast

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private var login = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupActionBar()
        // Set up the login form.
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
        already_signed_up.setOnClickListener {
            login = !login
            email_sign_in_button.textResource = if (login) R.string.log_in else R.string.action_sign_in_short
            already_signed_up.textResource = if (login) R.string.need_to_sign_in else R.string.already_have_an_account
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private fun setupActionBar() {
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)

            if (login) {
                loginUser(emailStr, passwordStr)
            } else {
                signUpUser(emailStr, passwordStr)
            }
        }
    }

    private fun signUpUser(emailStr: String, passwordStr: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, { task ->
            showProgress(false)
            addUserToDB(task)
            handleResponse(task)
        })
    }

    private fun loginUser(emailStr: String, passwordStr: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, { task ->
            showProgress(false)
            handleResponse(task)
        })
    }

    private fun LoginActivity.handleResponse(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            toast("Login successful")
            finish()
        } else {
            toast(task.exception?.localizedMessage.toString())
        }
    }

    private fun addUserToDB(task: Task<AuthResult>) {
        if (task.isSuccessful) {
//            val user = task.result!!.user

            // TODO: Do this with a DB trigger instead
            // add the user to the Firebase DB (separate from the Firebase Auth DB)
//            FirebaseDatabase.getInstance().reference.child("users").child(user.uid).setValue(BeaconUser(user.displayName, user.email, ArrayList()))
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }
}
