package com.company.zeerorg.mynotes.model

/**
 * Created by zeerorg on 7/3/17.
 */
interface FileLogInterface {

    fun logCreateNote(id: Long)

    fun logUpdateNote(id: Long)

    fun executeLog(createFun: (id: Long) -> Unit, updateFun: (id: Long) -> Unit, deleteFun: (id: Long) -> Unit)
}