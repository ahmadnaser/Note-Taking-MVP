package com.company.zeerorg.mynotes.view

/**
 * Created by zeerorg on 6/13/17.
 *
 * It lets presenter communicate with View
 */
interface NoteDependencyInterface {
    fun updateRecycler()

    fun notifyDelete(position: Int)

    fun getFileDirectory() : String

    fun finishActivity()
}