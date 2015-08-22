package com.averageloser.tjfnotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.averageloser.tjfnotes.Model.NotesModel;
import com.averageloser.tjfnotes.UI.NoteDetailFragment;
import com.averageloser.tjfnotes.UI.NoteListFragment;

/**
 * Created by tj on 8/21/15.
 *
 * The main Activity of the notes application.   This activity will act as a controller for all the
 * fragments.
 */
public class NotesMainActivity extends AppCompatActivity implements NoteListFragment.FloatingActionButtonListener {
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                    .addToBackStack(null).commit();
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
}
