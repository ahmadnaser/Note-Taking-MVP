package com.example.zeerorg.firstkotlin.main

/**
 * Created by zeerorg on 7/3/17.
 */
interface HelperMethodsInterface {
    fun checkOnline(successFun: () -> Unit, failFun: () -> Unit)
}