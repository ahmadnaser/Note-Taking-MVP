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
class LoginActivity : AppCompatActivity(), LoginDependencyInterface {
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

    val presenter: LoginPresenterInterface = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.checkLogin()

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
                    startNoteActivity()
                } else {
                    Log.e("MyApp", "User logged in through Facebook!")
                    startNoteActivity()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    override fun showProgress(show: Boolean) {
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
        mLoginFormView.visibility = if (show) View.GONE else View.VISIBLE
    }

    fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE, {findViewById(idRes) as T} )
    }

    override fun startNoteActivity(){
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
    }

    override fun emailError(err: String) {
        mEmailView.error = err
    }

    override fun passwordError(err: String) {
        mPasswordView.error = err
    }
}

