package com.example.zeerorg.firstkotlin.presenter

import android.util.Log
import com.example.zeerorg.firstkotlin.model.UserRepository
import com.example.zeerorg.firstkotlin.model.UserRepositoryInterface
import com.example.zeerorg.firstkotlin.view.LoginDependencyInterface

/**
 * Created by zeerorg on 6/14/17.
 */
class LoginPresenter(val loginView: LoginDependencyInterface, val loginRepo: UserRepositoryInterface = UserRepository()) : LoginPresenterInterface {

    override fun checkLogin() {
        if(loginRepo.isLoggedIn())
            loginView.startNoteActivity()
    }

    override fun attemptLogin(email: String, password: String) {
        // TODO : validate email and password
        loginView.showProgress(true)
        if(password.length < 6) {
            loginView.passwordError("Password too short")
        } else if(!email.contains("@")) {
            loginView.emailError("Not Valid EMail")
        } else {
            loginRepo.exists(email,
                    {
                        Log.e("myApp", "Login trying")
                        loginRepo.login(email, password, this::success, this::fail)
                    },
                    {
                        Log.e("myApp", "Sign Up trying")
                        loginRepo.signUp(email, password, this::success, this::fail)
                    }
            )
        }
    }

    private fun success(){
        Log.e("myApp", "Login success")
        loginView.startNoteActivity()
    }

    private fun fail() {
        Log.e("myApp", "Login fail")
        loginView.showProgress(false)
    }
}