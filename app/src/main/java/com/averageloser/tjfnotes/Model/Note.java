package com.averageloser.tjfnotes.Model;

/**
 * Created by tj on 8/20/15.
 *
 * This is the pojo for a note.
 */
public class Note {

    private String title;
    private String body;
    private String id;

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return getTitle() + " " + getBody();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
