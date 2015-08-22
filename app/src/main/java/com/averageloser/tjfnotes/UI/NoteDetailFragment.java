package com.averageloser.tjfnotes.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.averageloser.tjfnotes.R;

public class NoteDetailFragment extends Fragment {
    private EditText title;
    private EditText body;

    public NoteDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_detail, container, false);

        title = (EditText) rootView.findViewById(R.id.note_title);

        body = (EditText) rootView.findViewById(R.id.note_body);

        return rootView;
    }

    public String getTitle() {
        return title.getText().toString();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public String getBody() {
        return body.getText().toString();
    }

    public void setBody(String body) {
        this.body.setText(body);
    }
}
