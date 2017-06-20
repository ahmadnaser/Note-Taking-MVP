package com.example.zeerorg.firstkotlin.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


/**
 * Created by zeerorg on 6/9/17.
 */

open class Note : RealmObject() {

    @PrimaryKey
    var id : Int = 0

    var data : String = ""

    var uploaded : Boolean = false
}