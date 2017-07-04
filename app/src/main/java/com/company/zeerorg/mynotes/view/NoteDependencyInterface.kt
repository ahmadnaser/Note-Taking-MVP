package com.company.zeerorg.mynotes.view

/**
 * Created by zeerorg on 6/13/17.
 *
 * It lets presenter communicate with View
 */
interface NoteDependencyInterface {
    fun updateRecycler()

    fun getFileDirectory() : String
}