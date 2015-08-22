package com.averageloser.tjfnotes.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.averageloser.tjfnotes.R;

public class NoteListFragment extends ListFragment {
    public interface FloatingActionButtonListener {
        void onFabClicked();
    }

    private FloatingActionButton newNoteButton;
    private FloatingActionButtonListener fabListener;

    public NoteListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            fabListener = (FloatingActionButtonListener) getActivity();
        } catch(ClassCastException e) {
            Log.e("ClassCastException", "Activities must implement all interfaces");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        fabListener = null;
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

    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
    }

    public void newNoteButton(View v) {
        //Here I call back to the activity to tell it that a user wants to add a new note.
        Log.i("new note button", "clicked");

        fabListener.onFabClicked();
    }
}
