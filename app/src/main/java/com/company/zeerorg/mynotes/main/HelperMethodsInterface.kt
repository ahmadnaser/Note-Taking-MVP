package com.company.zeerorg.mynotes.main

/**
 * Created by zeerorg on 7/3/17.
 */
interface HelperMethodsInterface {
    fun checkOnline(successFun: () -> Unit, failFun: () -> Unit)
}