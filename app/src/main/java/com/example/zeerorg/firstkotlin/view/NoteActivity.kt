package com.example.zeerorg.firstkotlin.view

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import com.example.zeerorg.firstkotlin.R
import com.example.zeerorg.firstkotlin.presenter.Presenter
import com.example.zeerorg.firstkotlin.presenter.PresenterInterface
import com.example.zeerorg.firstkotlin.presenter.PresenterUsingViewInterface


/**
 *
 * No use of abstracting NoteActivityInterface but still I'm doing it idk why??
 *
 */

class NoteActivity : AppCompatActivity(), NoteActivityInterface, PresenterUsingViewInterface {

    //val actionBar by lazy(LazyThreadSafetyMode.NONE, { supportActionBar })
    val actionBar by threadSafeLazy { supportActionBar }
    val newNoteBtn by lazy(LazyThreadSafetyMode.NONE, { findViewById(R.id.new_note_btn) as FloatingActionButton }) // can be done how I've done with textView
    val recyclerView by bind<RecyclerView>(R.id.notes_recycler)
    val presenter: PresenterInterface = Presenter(this)  // Here goes my MVP architecture and testability
                                                        // This is why we use f***in Dagger
    val adapter = NotesAdapter(presenter.getNotesList())

    //val items = NoteRepository.getListNotesItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.title = "Notes"
        newNoteBtn.setOnClickListener {
            clickedFab()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()

//        recyclerView.adapter = fastAdapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        fastAdapter.add(items)
//        fastAdapter.notifyAdapterDataSetChanged()
    }

    override fun clickedFab() {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)

        builder.setView(input)
        builder.setPositiveButton("Create", { _: DialogInterface, _: Int -> presenter.addNote(input.text.toString()) })
        builder.setNegativeButton("Cancel", { dialog: DialogInterface, _: Int -> dialog.cancel()})

        builder.show()
    }

    override fun updateRecycler() {
        adapter.notifyDataSetChanged()
    }

    fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE, {findViewById(idRes) as T} )
    }

    fun <T> threadSafeLazy( yourFun: () -> T ) = lazy(LazyThreadSafetyMode.NONE, yourFun)
}
