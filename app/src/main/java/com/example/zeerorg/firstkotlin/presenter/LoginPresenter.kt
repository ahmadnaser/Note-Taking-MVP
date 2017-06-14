package com.example.zeerorg.firstkotlin.presenter

import com.example.zeerorg.firstkotlin.model.UserRepository
import com.example.zeerorg.firstkotlin.model.UserRepositoryInterface

/**
 * Created by zeerorg on 6/14/17.
 */
class LoginPresenter(val loginRepo: UserRepositoryInterface = UserRepository()) : LoginPresenterInterface {

    override fun checkLogin(): Boolean {
        return loginRepo.isLoggedIn()
    }

    override fun attemptLogin(email: String, password: String) {
        // TODO : validate email and password
        if(password.length < 6) {
            // TODO : call a method for view error
        } else {
            loginRepo.exists(email,
                    {
                        loginRepo.login(email, password, this::success, this::fail)
                    },
                    {
                        loginRepo.signUp(email, password, this::success, this::fail)
                    }
            )
        }

    }

    private fun success(){
        // TODO : return on success
    }

    private fun fail() {

    }
}