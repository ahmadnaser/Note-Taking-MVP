package com.company.zeerorg.mynotes.view

import android.app.ActionBar
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.company.zeerorg.mynotes.R
import com.company.zeerorg.mynotes.model.Note
import com.company.zeerorg.mynotes.presenter.LoginPresenterInterface
import com.company.zeerorg.mynotes.presenter.Presenter
import com.company.zeerorg.mynotes.presenter.PresenterInterface
import com.company.zeerorg.mynotes.view.recyclers.NotesAdapter


/**
 *
 * No use of abstracting NoteActivityInterface but still I'm doing it idk why?? |||| UPDATE : Removed Note dependency Interface
 *
 */

class NoteActivity : AppCompatActivity(), NoteDependencyInterface {

    val recyclerView by bind<RecyclerView>(R.id.notes_recycler)
    val newNoteBtn by lazy(LazyThreadSafetyMode.NONE, { findViewById(R.id.new_note_btn) as Button })
    lateinit var presenter : PresenterInterface// Here goes my MVP architecture and testability
                                                        // This is why we use f***in Dagger
    lateinit var adapter: NotesAdapter
    lateinit var userPresenter: LoginPresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(this)
        adapter = NotesAdapter(presenter.getNotesList().asReversed(), this, this::editNote, this::delNote)

        presenter.startLoad()
        supportActionBar?.title = "Notes"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black)))
        setCustomActionBar()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter.notifyDataSetChanged()

        newNoteBtn.setOnClickListener{
            clickedFab()
        }

//        val itemTouch = TouchHelper(adapter)
//        val itemTouchHelper = ItemTouchHelper(itemTouch)
//        itemTouchHelper.attachToRecyclerView(recyclerView)


//        recyclerView.adapter = fastAdapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        fastAdapter.add(items)
//        fastAdapter.notifyAdapterDataSetChanged()
    }

    private fun editNote(org: Note) : Unit {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val mainLayout = inflater.inflate(R.layout.note_dialog, null)
        val input = mainLayout.findViewById(R.id.edit_data) as EditText
        val inpTitle = mainLayout.findViewById(R.id.edit_title) as EditText

        input.setText(org.data)
        inpTitle.setText(org.title)
        builder.setView(mainLayout)
        builder.setPositiveButton("Update", { _, _ -> presenter.updateNote(org, input.text.toString(), inpTitle.text.toString()) })
        builder.setNegativeButton("Cancel", { dialog, _ -> dialog.cancel()})

        builder.show()
    }

    private fun delNote(org: Note) : Unit {
        presenter.deleteNote(org)
    }

    private fun clickedFab() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val mainLayout = inflater.inflate(R.layout.note_dialog, null)
        val input = mainLayout.findViewById(R.id.edit_data) as EditText
        val inpTitle = mainLayout.findViewById(R.id.edit_title) as EditText

        builder.setView(mainLayout)
        builder.setPositiveButton("Create", { _, _ -> presenter.addNote(input.text.toString(), inpTitle.text.toString()) })
        builder.setNegativeButton("Cancel", { dialog, _ -> dialog.cancel()})

        builder.show()
    }

    private fun setCustomActionBar() {
        val inflater = this.layoutInflater
        val mainLayout = inflater.inflate(R.layout.main_action_bar, null)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.customView = mainLayout.findViewById(R.id.main_view)
        mainLayout.findViewById(R.id.user_icon).setOnClickListener {
            val builder = AlertDialog.Builder(this).create()
            val userDialogLayout = inflater.inflate(R.layout.user_dialog, null)

            userDialogLayout.findViewById(R.id.logout_btn).setOnClickListener {
                builder.dismiss()
                presenter.logOut()
            }

            (userDialogLayout.findViewById(R.id.user_text) as TextView).text = presenter.getUsername()

            builder.setView(userDialogLayout.findViewById(R.id.main_view))
            builder.show()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun updateRecycler() {
        adapter.notifyDataSetChanged()
    }

    override fun notifyDelete(position: Int) {
        adapter.notifyItemRemoved(position)
    }

    override fun getFileDirectory(): String {
        Log.e("Activity", this.filesDir.absolutePath)
        return this.filesDir.absolutePath
    }

    override fun finishActivity() {
        finish()
    }

    fun <T : View> AppCompatActivity.bind(@IdRes idRes: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE, {findViewById(idRes) as T} )
    }

    fun <T> threadSafeLazy( yourFun: () -> T ) = lazy(LazyThreadSafetyMode.NONE, yourFun)
}
