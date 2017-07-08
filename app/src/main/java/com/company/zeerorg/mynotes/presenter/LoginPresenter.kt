package com.company.zeerorg.mynotes.presenter

import android.util.Log
import com.company.zeerorg.mynotes.main.HelperMethods
import com.company.zeerorg.mynotes.main.HelperMethodsInterface
import com.company.zeerorg.mynotes.model.UserRepository
import com.company.zeerorg.mynotes.model.UserRepositoryInterface
import com.company.zeerorg.mynotes.view.LoginDependencyInterface

/**
 * Created by zeerorg on 6/14/17.
 */
class LoginPresenter(val loginView: LoginDependencyInterface,
                     val loginRepo: UserRepositoryInterface = UserRepository(),
                     val helpers: HelperMethodsInterface = HelperMethods()) : LoginPresenterInterface {

    override fun checkLogin() {
        if(loginRepo.isLoggedIn())
            loginView.startNoteActivity()
    }

    override fun attemptLogin(email: String, password: String) {
        // TODO : validate email and password
        loginView.showProgress(true)
        if(password.length < 6) {
            loginView.passwordError("Password too short")
            loginView.showProgress(false)
        } else if(!email.contains("@")) {
            loginView.emailError("Not Valid EMail")
            loginView.showProgress(false)
        } else {
            helpers.checkOnline({
                loginRepo.login(email, password, this::success, this::fail)
            }, {
                loginView.noConnectionErr()
            })
        }
    }

    override fun getUserName(): String {
        return loginRepo.getUser().username
    }

    override fun logOut() {
        loginRepo.logOut()

    }

    override fun attemptSignUp(email: String, password: String) {
        loginView.showProgress(true)
        if(password.length < 6) {
            loginView.passwordError("Password too short")
            loginView.showProgress(false)
        } else if(!email.contains("@")) {
            loginView.emailError("Not Valid EMail")
            loginView.showProgress(false)
        } else {
            helpers.checkOnline({
                loginRepo.signUp(email, password, this::success, this::fail)
            }, {
              loginView.noConnectionErr()
            })
        }
    }

    private fun success(){
        Log.e("myApp", "Login success")
        loginView.startNoteActivity()
    }

    private fun fail(code: Int) {
        Log.e("myApp", "Login fail")
        loginView.showProgress(false)
        when(code) {  // from : http://parseplatform.org/Parse-SDK-Android/api/constant-values.html
                        // I don't want to use parse exception as a dependency here so I had to do this
            205, 200 -> loginView.emailError("Register First")
            203, 202 -> loginView.emailError("Email Taken")
            201 -> loginView.passwordError("Password Missing")
            142 -> loginView.emailError("Validation Error")
            100 -> loginView.noConnectionErr()
            208 -> loginView.emailError("Account Exists")
        }
    }
}