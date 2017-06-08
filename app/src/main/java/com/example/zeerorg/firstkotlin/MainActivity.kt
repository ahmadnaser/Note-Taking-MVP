package com.example.zeerorg.firstkotlin

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter


class MainActivity : AppCompatActivity() {

    //val actionBar by lazy(LazyThreadSafetyMode.NONE, { supportActionBar })
    val actionBar by threadSafeLazy { supportActionBar }
    val newNoteBtn by lazy(LazyThreadSafetyMode.NONE, { findViewById(R.id.new_note_btn) as FloatingActionButton }) // can be done how I've done with textView
    val recyclerView by bind<RecyclerView>(R.id.notes_recycler)

    val fastAdapter = FastItemAdapter<NotesItem>()
    val items: List<NotesItem> = listOf(NotesItem("Hello"), NotesItem("How Are you?"), NotesItem("Have a good Day"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.title = "Notes"
        newNoteBtn.setOnClickListener {  }

        recyclerView.adapter = fastAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        fastAdapter.add(items)
        fastAdapter.notifyAdapterDataSetChanged()
    }

    fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE, {findViewById(idRes) as T} )
    }

    fun <T> threadSafeLazy( yourFun: () -> T ) = lazy(LazyThreadSafetyMode.NONE, yourFun)
}
