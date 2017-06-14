package com.example.zeerorg.firstkotlin.view

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo

import com.example.zeerorg.firstkotlin.R

import android.app.Activity
import android.content.Intent
import android.support.annotation.IdRes
import android.util.Log
import android.widget.*
import com.example.zeerorg.firstkotlin.presenter.LoginPresenter
import com.example.zeerorg.firstkotlin.presenter.LoginPresenterInterface
import com.parse.ParseException
import com.parse.ParseFacebookUtils
import com.parse.ParseUser

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity() : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: Boolean = false

    // UI references.
    val mEmailView by bind<AutoCompleteTextView>(R.id.email)
    val mPasswordView by bind<EditText>(R.id.password)
    val mProgressView by bind<View>(R.id.login_progress)
    val mLoginFormView by bind<View>(R.id.login_form)
    val fbLoginButton by bind<Button>(R.id.login_button)

    val presenter: LoginPresenterInterface = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(presenter.checkLogin()) {
            startNoteActivity()
        }

        setContentView(R.layout.activity_login)

        mPasswordView.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                presenter.attemptLogin(mEmailView.text.toString(), mPasswordView.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        val mEmailSignInButton = findViewById(R.id.email_sign_in_button) as Button
        mEmailSignInButton.setOnClickListener { presenter.attemptLogin(mEmailView.text.toString(), mPasswordView.text.toString()) }

        //fbLoginButton.setReadPermissions(email)
        fbLoginButton.setOnClickListener{
            ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, { user: ParseUser?, _: ParseException ->
                if (user == null) {
                    Log.e("MyApp", "Uh oh. The user cancelled the Facebook login.")
                } else if (user.isNew) {
                    Log.e("MyApp", "User signed up and logged in through Facebook!")
                    val intent = Intent(this, NoteActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("MyApp", "User logged in through Facebook!")
                    val intent = Intent(this, NoteActivity::class.java)
                    startActivity(intent)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask) {
            return
        }

        // Reset errors.
        mEmailView.error = null
        mPasswordView.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView.text.toString()
        val password = mPasswordView.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            val user = ParseUser()
            user.username = mEmailView.text.toString()
            user.setPassword(mPasswordView.text.toString())
            user.email = mEmailView.text.toString()
            mAuthTask = true
            Log.e("MyApp", "Trying log in")
            user.signUpInBackground{ e ->
                if (e == null) {
                    val intent = Intent(this, NoteActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("MyApp", e.code.toString() + " :: ")

                    when (e.code) {
                        ParseException.USERNAME_TAKEN -> {
                            mEmailView.error = "This email exists"
                        }// report error
                        else -> {
                            ParseUser.logInInBackground(mEmailView.text.toString(), mPasswordView.text.toString(), { user, _ ->
                                if(user != null) {
                                    val intent = Intent(this, NoteActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    showProgress(false)
                                    Toast.makeText(this, "Something went wrong try again.", Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    mAuthTask = false
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 6
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
        mLoginFormView.visibility = if (show) View.GONE else View.VISIBLE
    }

    fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE, {findViewById(idRes) as T} )
    }

    private fun startNoteActivity(){
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
    }
}

