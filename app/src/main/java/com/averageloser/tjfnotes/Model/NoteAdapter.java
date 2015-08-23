package com.averageloser.tjfnotes.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.averageloser.tjfnotes.R;

import java.util.List;

/**
 * Created by tj on 8/23/15.
 */
public class NoteAdapter extends ArrayAdapter<Note> {
    private Context context;
    private List<Note> notes;
    private int resource;


    public NoteAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);

        this.context = context;

        notes = objects;

        this.resource = resource;
    }

    @Override
    public Note getItem(int position) {
        return notes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.note_title_list_row);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) (convertView.getTag());
        }

        Note note = getItem(position);

        viewHolder.title.setText(note.getTitle());

        return convertView;
    }

    static class ViewHolder {
        TextView title;
    }
}
