package com.averageloser.tjfnotes.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.averageloser.tjfnotes.Model.Note;
import com.averageloser.tjfnotes.Model.NoteAdapter;
import com.averageloser.tjfnotes.R;

import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends ListFragment implements AbsListView.OnScrollListener {
    public interface NotesListFragmentListener {
        void onReadyForNotes();
        void onNoteClicked(Note note);
        void onRequestNoteDelete(Note note);
    }

    public interface FloatingActionButtonListener {
        void onFabClicked();
    }

    private FloatingActionButton newNoteButton;
    private FloatingActionButtonListener fabListener;
    private NotesListFragmentListener notesListener;
    private List<Note> notesList;
    private NoteAdapter noteAdapter;

    public NoteListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fabListener = (FloatingActionButtonListener) getActivity();

            notesListener = (NotesListFragmentListener) getActivity();
        } catch(ClassCastException e) {
            Log.e("ClassCastException", "Activities must implement all interfaces");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        fabListener = null;

        notesListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        newNoteButton = (FloatingActionButton) view.findViewById(R.id.new_note_button);
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNoteButton(v);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notesList = new ArrayList();

        //set the adapter for the Notes.
        noteAdapter = new NoteAdapter(getActivity(),R.layout.note_list_row, notesList);

        setListAdapter(noteAdapter);

        //inform my activity that I want the list of notes to be loaded.
        notesListener.onReadyForNotes();

        getListView().setOnScrollListener(this);

        //register the listview for a context menu so users can long press to delete notes.
        registerForContextMenu(getListView());
    }


    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        //Notify the listener which note was clicked.
        notesListener.onNoteClicked(notesList.get(position));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Just call back to the activity and deltet this note.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        notesListener.onRequestNoteDelete(notesList.get(info.position)); //I should just call removeNote() in this class. :)

        return super.onContextItemSelected(item);
    }

    public void newNoteButton(View v) {
        //Here I call back to the activity to tell it that a user wants to add a new note.
        Log.i("new note button", "clicked");

        fabListener.onFabClicked();
    }

    public void addNote(Note note) {
        //called by the activity when in dual pane mode to add a note to the list, instead of having to download them all again.
        notesList.add(note);

        noteAdapter.notifyDataSetChanged();
    }
    //Called by activity to remove note from list of notes and update adapter.
    public void removeNote(Note note) {
        notesList.remove(note);

        noteAdapter.notifyDataSetChanged();
    }

    //Called by activity to update the adapter with a new list of notes.
    public void updateAdapter(List<Note> notes) {
        notesList.clear();
        notesList.addAll(notes);

        Log.i("Notes array", String.valueOf(notesList.size()));

        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                newNoteButton.setVisibility(View.VISIBLE);

                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                newNoteButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

}
