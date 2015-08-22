package com.averageloser.tjfnotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.averageloser.tjfnotes.Model.Note;
import com.averageloser.tjfnotes.Model.NotesModel;
import com.averageloser.tjfnotes.UI.NoteDetailFragment;
import com.averageloser.tjfnotes.UI.NoteListFragment;

import java.util.List;

/**
 * Created by tj on 8/21/15.
 * <p/>
 * The main Activity of the notes application.   This activity will act as a controller for all the
 * fragments.
 */
public class NotesMainActivity extends AppCompatActivity implements NotesModel.NotesModelListener,
        NoteListFragment.FloatingActionButtonListener, NoteListFragment.NotesListFragmentListener {
    private boolean dualPane;
    private NoteListFragment noteListFragment;
    private NoteDetailFragment noteDetailFragment;
    private NotesModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_main);

        //instantiate the list fragment.
        noteListFragment = new NoteListFragment();

        //instantiate the note detail fragment.
        noteDetailFragment = new NoteDetailFragment();

        //The notes model for manipulating notes.
        model = new NotesModel();
        model.addNotesModelListener(this);

        //figure out whether or not we are in dual tablet landscape mode.
        dualPane = (findViewById(R.id.note_detail_container) != null);

        Log.i("dual pane", String.valueOf(dualPane));

        if (dualPane) {
            //Add the detail and list Fragments to their containers.
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.note_list_container, noteListFragment)
                    .add(R.id.note_detail_container, noteDetailFragment, "NDF")
                    .commit();
        } else {
            //I am in single pane, so add the listFragment
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, noteListFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                //dont to anything, unless a user is adding note details.
                if (noteDetailFragment.isVisible()) {
                    //if the note detail fragment is visible, i can check for data.
                    if (!noteDetailFragment.getTitle().isEmpty()) {
                        //the user has to at least type in something for a  title, save a note..
                        model.saveNote(noteDetailFragment.getTitle(), noteDetailFragment.getBody());

                        if (!dualPane) {
                            getSupportFragmentManager().popBackStack();
                        }
                    }
                }

                break;

            //case for deleting all notes.
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFabClicked() {
        /*A user wants to add a new note.  If we are in single pane mode, replace the current
        fragment with the note details fragment, otherwise just save the note.*/

        if (dualPane) {
            //the notes detail fragment is already in the layout, so clear its data for a new note.
            noteDetailFragment.setTitle("");
            noteDetailFragment.setBody("");
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, noteDetailFragment)
                    .addToBackStack("details").commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (dualPane) {
            //if I am in dual pane mode, just finish the activity because the back stack may hav eentries..
            finish();
        }

        super.onBackPressed();
    }

    ///////////////////////////////////start model manipulation callbacks//////////////////////////////
    @Override
    public void onNoteSaved(Note note) {
        Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show();

        if (dualPane) {
            //add the note to the list of notes maintained by the listfragment.
            noteListFragment.addNote(note);
        }
    }

    @Override
    public void onNoteEdited(Note note) {

    }

    @Override
    public void onNoteDeleted(Note note) {
        //A note has been deleted, update the adapter and notify the user.

    }

    @Override
    public void onAllNotesDeleted() {

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

    @Override
    public void onNoteClicked(Note note) {

    }

    @Override
    public void onRequestNoteDelete(Note note) {
        //the user wants to delete a note.
        model.deleteNote(note);
    }
    ////////////////////////////////end NoteListFragmentListener methods///////////////////////////
}
