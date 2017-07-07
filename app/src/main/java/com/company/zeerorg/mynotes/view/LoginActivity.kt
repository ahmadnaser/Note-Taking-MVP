package com.company.zeerorg.mynotes.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo

import com.company.zeerorg.mynotes.R

import android.app.Activity
import android.content.Intent
import android.support.annotation.IdRes
import android.widget.*
import com.company.zeerorg.mynotes.presenter.LoginPresenter
import com.company.zeerorg.mynotes.presenter.LoginPresenterInterface
import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoginDependencyInterface {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    val mEmailView by bind<AutoCompleteTextView>(R.id.email)
    val mPasswordView by bind<EditText>(R.id.password)
    val mProgressView by bind<View>(R.id.login_progress)
    val mLoginFormView by bind<View>(R.id.login_form)

    // Presenter
    val presenter: LoginPresenterInterface = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
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

        val mEmailSignUpBtn = findViewById(R.id.email_sign_up_button) as Button
        mEmailSignUpBtn.setOnClickListener {
            presenter.attemptSignUp(mEmailView.text.toString(), mPasswordView.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun emailError(err: String) {
        mEmailView.error = err
    }

    override fun passwordError(err: String) {
        mPasswordView.error = err
    }

    override fun noConnectionErr() {
        val alertDialog = AlertDialog.Builder(this).create()

        alertDialog.setTitle("Info")
        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again")
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
        alertDialog.setButton("OK") { dialog, which -> finish() }

        alertDialog.show()
    }
}

