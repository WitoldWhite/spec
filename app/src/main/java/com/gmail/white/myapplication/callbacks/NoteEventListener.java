package com.gmail.white.myapplication.callbacks;

import com.gmail.white.myapplication.model.Note;

public interface NoteEventListener {
    void onNoteClick(Note note);
    void onNoteLongClick(Note note);
}
