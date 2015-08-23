package com.averageloser.tjfnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.averageloser.tjfnotes.Model.Note;
import com.averageloser.tjfnotes.Model.NotesModel;
import com.averageloser.tjfnotes.UI.NoteListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tj on 8/21/15.
 * <p>
 * The main Activity of the notes application.   This activity will act as a controller for all the
 * fragments.
 */
public class NotesListActivity extends AppCompatActivity implements NotesModel.NotesModelListener,
        NoteListFragment.FloatingActionButtonListener, NoteListFragment.NotesListFragmentListener {

    private static final int DETAIL_REQUEST = 1;
    private NoteListFragment noteListFragment;
    private NotesModel model;
    private Note note;  //the note I send to the Details Fragment when someone clicks the list in portrait mode.
    private boolean noteClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_list_activity_main);

        //instantiate the fragment.
        noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.note_list_fragment);

        //The notes model for manipulating notes.
        model = new NotesModel();
        model.addNotesModelListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case for deleting all notes.
            case R.id.delete_all:
                model.deleteAllNotes();

                break;

            case R.id.refresh:
                model.getAllNotes();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFabClicked() {
        startActivityForResult(new Intent(this, NoteDetailActivity.class), DETAIL_REQUEST);
    }


    ///////////////////////////////////start model manipulation callbacks//////////////////////////////
    @Override
    public void onNoteSaved(Note note) {
        Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show();

        noteListFragment.addNote(note);
    }

    @Override
    public void onNoteEdited(Note note) {

    }

    @Override
    public void onNoteDeleted(Note note) {
        //A note has been deleted, update the adapter and notify the user.
        noteListFragment.removeNote(note);
    }

    @Override
    public void onAllNotesDeleted() {
        /*All the notes have been deleted, so clear the list fragment of notes as well.  I can just
        call onAllNotesAcquired with an empty list of notes.
         */

        onAllNotesAcquired(new ArrayList<Note>());
    }

    @Override
    public void onAllNotesAcquired(List<Note> notes) {
        Log.i("onAllNotesAcquired()", "called");

        //Here I pass the list of notes ot the NotesListFragment for processing.
        noteListFragment.updateAdapter(notes);
    }

    @Override
    public void onError() {
        Log.i("Model error", "Connection error with model");
    }
    /////////////////////////////////////end model manipulation callbacks///////////////////////////

    ////////////////////////////start NoteListFragmentListener methods/////////////////////////////
    @Override
    public void onReadyForNotes() {
        Log.i("onReadyForNotes()", "called");

        //Get a list of all notes, then pass it to the ListFragment for processing.
        model.getAllNotes();
    }

    //the user clicked a note in the list.  Start the note details activity and pass the note info.
    @Override
    public void onNoteClicked(Note note) {
        Intent detailIntent = new Intent(this, NoteDetailActivity.class);
        detailIntent.putExtra("title", note.getTitle());
        detailIntent.putExtra("body", note.getBody());

        startActivity(detailIntent);
    }

    @Override
    public void onRequestNoteDelete(Note note) {
        //the user wants to delete a note.
        model.deleteNote(note);
    }
    ////////////////////////////////end NoteListFragmentListener methods///////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //The user is saving a note.  Get the data and save it.
            String title = data.getStringExtra("title");

            String body = data.getStringExtra("body");

            model.saveNote(title, body);
        }
    }
}
