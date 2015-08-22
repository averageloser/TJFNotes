package com.averageloser.tjfnotes.Model;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tj on 8/20/15.
 */
public class NotesModel {
    public interface NotesModelListener {
        void onNoteSaved(Note note);

        void onNoteEdited(Note note);

        void onNoteDeleted(Note note);

        void onAllNotesDeleted();

        void onAllNotesAcquired(List<Note> notes);

        void onError(); //Maybe I should be more specific?
    }

    private List<NotesModelListener> listeners = Collections.synchronizedList(new ArrayList<NotesModelListener>());

    public static final String CLASSNAME = "Note";

    public void addNotesModelListener(NotesModelListener listener) {
        listeners.add(listener);
    }

    public void removeNotesModelListener(NotesModelListener listener) {
        listeners.remove(listener);
    }

    public void saveNote(final String title, final String body) {
        final Note note = new Note(title, body);

        final ParseObject parseNote = new ParseObject(CLASSNAME);
        parseNote.put("title", note.getTitle());
        parseNote.put("body", note.getBody());
        parseNote.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //Notify listeners.
                for (NotesModelListener listener : listeners) {
                    if (e == null) {
                        //the operation completed successfully.
                        note.setId(parseNote.getObjectId());

                        listener.onNoteSaved(note);
                    } else {
                        //operation did not complete successfully.
                        listener.onError();
                    }
                }
            }
        });
    }

    //Find note by id and delete it from parse, then notify listeners.
    public void deleteNote(final Note note) {
        ParseQuery<ParseObject> objQuery = ParseQuery.getQuery(CLASSNAME);
        objQuery.getInBackground(note.getId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    obj.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Notify listeners.
                            for (NotesModelListener listener : listeners) {
                                if (e == null) {
                                    //the operation completed successfully.
                                    listener.onNoteDeleted(note);
                                } else {
                                    //operation did not complete successfully.
                                    listener.onError();
                                }
                            }
                        }
                    });
                } else {
                    Log.i("error", "Can't delete note!");
                }
            }
        });
    }

    public void editNote(final Note note, final String title, final String body) {
        //Get note by id.
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASSNAME);
        // Retrieve the object by id
        query.getInBackground(note.getId(), new GetCallback<ParseObject>() {
            public void done(ParseObject noteObj, ParseException e) {

                if (e == null) {

                    /*set the new data for the note locally in the notes list.  I don't have to
                    redownload the list of notes every time I edit one, as long as it is updated
                    in both places. */
                    note.setTitle(title);
                    note.setBody(body);

                    /*Make changes online.  The Parse documentation says....
                      Parse automatically figures out which data has changed so only "dirty" fields will
                      be transmitted during a save. You don't need to worry about squashing data in the
                      cloud that you didn't intend to update.
                     */
                    noteObj.put("title", title);
                    noteObj.put("body", body);
                    noteObj.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Notify listeners.
                            for (NotesModelListener listener : listeners) {
                                if (e == null) {
                                    //the operation completed successfully.

                                    listener.onNoteEdited(note);
                                } else {
                                    //operation did not complete successfully.
                                    listener.onError();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void getAllNotes() {
        ParseQuery<ParseObject> allNotesQuery = ParseQuery.getQuery(CLASSNAME);
        allNotesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                final List<Note> notesList = new ArrayList<Note>();

                if (e == null) {
                    for (ParseObject obj : list) {
                        Note note = new Note(obj.getString("title"), obj.getString("body"));
                        note.setId(obj.getObjectId());

                        notesList.add(note);
                    }
                }
                //Notify listeners.
                for (NotesModelListener listener : listeners) {
                    if (e == null) {
                        //the operation completed successfully.
                        listener.onAllNotesAcquired(notesList);
                    } else {
                        //operation did not complete successfully.
                        listener.onError();
                    }
                }
            }
        });

    }

    public void deleteAllNotes() {
        //first get a list of all parseobjects.
        ParseQuery<ParseObject> all = ParseQuery.getQuery(CLASSNAME);
        all.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ParseObject.deleteAllInBackground(list, new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //Notify listeners.
                                for (NotesModelListener listener : listeners) {
                                    if (e == null) {
                                        //the operation completed successfully.
                                        listener.onAllNotesDeleted();
                                    } else {
                                        //operation did not complete successfully.
                                        listener.onError();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
