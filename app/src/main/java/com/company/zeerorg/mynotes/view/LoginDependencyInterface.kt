package com.company.zeerorg.mynotes.view

/**
 * Created by zeerorg on 6/14/17.
 */
interface LoginDependencyInterface {
    fun showProgress(show: Boolean)

    fun startNoteActivity()

    fun emailError(err: String)

    fun passwordError(err: String)
}