package com.averageloser.tjfnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.averageloser.tjfnotes.Model.Note;
import com.averageloser.tjfnotes.UI.NoteDetailFragment;

/**
 * Created by tj on 8/23/15.
 */
public class NoteDetailActivity extends AppCompatActivity {
    private NoteDetailFragment noteDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_detail_activity_main);

        noteDetailFragment = (NoteDetailFragment) getSupportFragmentManager().findFragmentById(R.id.note_details_fragment);

        //Get the intent that started this activity to see if there is any data with which to populate the detail fragment.
        Intent detailIntent = getIntent();

        String title = detailIntent.getStringExtra("title");

        String body = detailIntent.getStringExtra("body");

        if (title != null && body != null) {
            //data is available for the details fragment.
            noteDetailFragment.setTitle(title);
            noteDetailFragment.setBody(body);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save:
                if (!noteDetailFragment.getTitle().isEmpty()  && ! noteDetailFragment.getBody().isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("title", noteDetailFragment.getTitle());
                    resultIntent.putExtra("body", noteDetailFragment.getBody());

                    setResult(RESULT_OK, resultIntent);

                    finish();
                }
        }

        return super.onOptionsItemSelected(item);
    }

}
