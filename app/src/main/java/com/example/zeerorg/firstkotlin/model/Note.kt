package com.example.zeerorg.firstkotlin.model

import com.example.zeerorg.firstkotlin.main.MyDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * Created by zeerorg on 6/9/17.
 */
@Table(name = "Note", database = MyDatabase::class)
class Note : BaseModel() {

    @PrimaryKey(autoincrement = true)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "data")
    var data: String = ""
}