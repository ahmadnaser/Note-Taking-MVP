package com.example.zeerorg.firstkotlin.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.zeerorg.firstkotlin.R
import com.example.zeerorg.firstkotlin.model.Note
import com.example.zeerorg.firstkotlin.presenter.Presenter
import com.example.zeerorg.firstkotlin.presenter.PresenterInterface
import com.example.zeerorg.firstkotlin.view.recyclers.NotesAdapter


/**
 *
 * No use of abstracting NoteActivityInterface but still I'm doing it idk why?? |||| UPDATE : Removed Note dependency Interface
 *
 */

class NoteActivity : Activity(), NoteDependencyInterface {

    val recyclerView by bind<RecyclerView>(R.id.notes_recycler)
    val newNoteBtn by lazy(LazyThreadSafetyMode.NONE, { findViewById(R.id.new_note_btn) as Button })
    lateinit var presenter : PresenterInterface// Here goes my MVP architecture and testability
                                                        // This is why we use f***in Dagger
    lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(this)
        presenter.startLoad()

        adapter = NotesAdapter(presenter.getNotesList().asReversed(), { org ->
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)
            Log.e("Activty", org.data)

            input.setText(org.data)
            builder.setView(input)
            builder.setPositiveButton("Update", { _, _ -> presenter.updateNote(org, input.text.toString()) })
            builder.setNegativeButton("Cancel", { dialog, _ -> dialog.cancel()})

            builder.show()
        })
        actionBar.title = "Notes"

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()

        newNoteBtn.setOnClickListener{
            clickedFab()
        }

//        recyclerView.adapter = fastAdapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        fastAdapter.add(items)
//        fastAdapter.notifyAdapterDataSetChanged()
    }

    private fun editNote(org: Note) : Unit {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        Log.e("Activty", org.data)

        input.setText(org.data)
        builder.setView(input)
        builder.setPositiveButton("Update", { _, _ -> presenter.updateNote(org, input.text.toString()) })
        builder.setNegativeButton("Cancel", { dialog, _ -> dialog.cancel()})

        builder.show()
    }

    private fun clickedFab() {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)

        builder.setView(input)
        builder.setPositiveButton("Create", { _, _ -> presenter.addNote(input.text.toString()) })
        builder.setNegativeButton("Cancel", { dialog, _ -> dialog.cancel()})

        builder.show()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun updateRecycler() {
        adapter.notifyDataSetChanged()
    }

    override fun getFileDirectory(): String {
        Log.e("Activity", this.filesDir.absolutePath)
        return this.filesDir.absolutePath
    }

    fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE, {findViewById(idRes) as T} )
    }

    fun <T> threadSafeLazy( yourFun: () -> T ) = lazy(LazyThreadSafetyMode.NONE, yourFun)
}
